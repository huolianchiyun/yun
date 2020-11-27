package com.yun.sys.modules.common.config;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.classmate.TypeResolver;
import com.yun.common.page.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.token-start-with}")
    private String tokenStartWith;

    @Value("${swagger.enable}")
    private Boolean enable;

    /**
     * 业务系统组名
     */
    @Value("${swagger.groupName:业务接口: 接口文档V1.0}")
    private String groupName;

    /**
     * 业务系统总的 api前缀
     */
    @Value("${swagger.baseUrl:'/api/**'}")
    private String baseUrl;

    @Bean
    public Docket api() {
        return getDocket(baseUrl, groupName, "业务接口", "", "1.0", enable);
    }


    @Bean
    public Docket yunrights_api() {
        return getDocket("/yun/**", "系统权限: yun-rights-接口文档V1.0", "权限接口",
                "一个简单且易上手的 Spring boot 权限框架", "1.0", enable);
    }

    private Docket getDocket(String baseUrl, String groupName, String title, String description, String version, Boolean enable) {
        return new Docket(DocumentationType.OAS_30)
                .enable(enable)
                .apiInfo(new ApiInfoBuilder()
                        .description(description)
                        .title(title)
                        .version(version)
                        .build())
                //修正Byte转string的Bug
                .directModelSubstitute(Byte.class, Integer.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(baseUrl))
                .build()
                .groupName(groupName)
//                .directModelSubstitute(LocalDateTime.class, String.class)
//                .directModelSubstitute(LocalDate.class, String.class)
//                .directModelSubstitute(LocalTime.class, String.class)
//                .directModelSubstitute(ZonedDateTime.class, String.class)
                .alternateTypeRules(
                        // https://www.codota.com/code/java/methods/springfox.documentation.spring.web.plugins.Docket/alternateTypeRules
                        // This rule is necessary to allow Swagger resolving Map<String, Object> types
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(Map.class, String.class, Object.class),
                                typeResolver.resolve(Map.class, String.class, WildcardType.class), Ordered.HIGHEST_PRECEDENCE)
                )
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(
                        Arrays.asList("https", "http")))
                // 授权信息设置，必要的header token等认证信息
                .securitySchemes(Collections.singletonList(
                        new ApiKey(tokenHeader, "token", "header")))
                // 授权信息全局token
                .securityContexts(Collections.singletonList(
                        SecurityContext.builder().securityReferences(Collections.singletonList(
                                new SecurityReference(tokenHeader, new AuthorizationScope[]{new AuthorizationScope("global", "")}))).build()
                ));
    }
}

/**
 * 将 PageInfo 转换展示在 swagger 中
 */
@Configuration
class SwaggerDataConfig {

    @Bean
    public AlternateTypeRuleConvention pageConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return CollectionUtil.newArrayList(newRule(resolver.resolve(PageInfo.class), resolver.resolve(Page.class)));
            }
        };
    }

    @ApiModel
    @Data
    private static class Page {
        @ApiModelProperty("页码 (0..N)")
        private Integer page;

        @ApiModelProperty("每页显示的数目")
        private Integer size;

        @ApiModelProperty("总条数")
        private Long total;
    }
}
