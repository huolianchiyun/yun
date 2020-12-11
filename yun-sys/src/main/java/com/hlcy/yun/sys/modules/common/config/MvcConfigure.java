package com.hlcy.yun.sys.modules.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;
import java.util.Optional;

import static com.hlcy.yun.sys.modules.rights.common.constant.RightsConstants.DICT_SUFFIX;

/**
 * WebMvcConfigurer
 */
@Configuration
@EnableWebMvc
public class MvcConfigure implements WebMvcConfigurer {

    /** 文件配置 */
    private final FileProperties properties;

    public MvcConfigure(FileProperties properties) {
        this.properties = properties;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        FileProperties.YunPath path = properties.getPath();
        String avatarUtl = "file:" + path.getAvatar().replace("\\","/");
        String pathUtl = "file:" + path.getPath().replace("\\","/");
        registry.addResourceHandler("/avatar/**").addResourceLocations(avatarUtl).setCachePeriod(0);
        registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new ConverterFactory<String, Enum>() {
            @Override
            public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {

                class EnumConverter<E extends Enum> implements Converter<String, E> {
                    private Class<E> enumType;

                    private EnumConverter(Class<E> enumType) {
                        this.enumType = enumType;
                    }

                    /**
                     * 判断枚举是否来自字典，若是，则去掉字典值后缀，防止枚举参数映射不到对应枚举
                     */
                    @Override
                    public E convert(String source) {
                        if (source.length() == 0) {
                            return null;
                        }
                        if (source.contains(DICT_SUFFIX)) {
                            source = source.substring(0, source.indexOf(DICT_SUFFIX));
                        }
                        String finalSource = source;
                        Optional<E> first = Arrays.stream(enumType.getEnumConstants()).filter(e -> e.toString().equals(finalSource)).findFirst();
                        return first.orElse(null);
                    }
                }
                return new EnumConverter<>(targetType);
            }
        });
    }
}
