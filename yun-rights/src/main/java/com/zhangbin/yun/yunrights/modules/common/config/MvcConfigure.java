package com.zhangbin.yun.yunrights.modules.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;
import java.util.Optional;

import static com.zhangbin.yun.yunrights.modules.rights.common.constant.RightsConstants.DICT_SUFFIX;

@Configuration
public class MvcConfigure implements WebMvcConfigurer {

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
                        if (source.endsWith(DICT_SUFFIX)) {
                            source = source.substring(0, source.lastIndexOf(DICT_SUFFIX));
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
