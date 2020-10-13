package com.zhangbin.yun.yunrights.modules.common.init;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.classmate.TypeResolver;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.ApiRightsDO;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;
import com.zhangbin.yun.yunrights.modules.rights.service.impl.ApiRightsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Conditional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.BuilderDefaults;
import springfox.documentation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.AbstractDocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.plugins.SpringIntegrationPluginNotPresentInClassPathCondition;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;
import static springfox.documentation.builders.BuilderDefaults.nullToEmptyMap;
import static springfox.documentation.spi.service.contexts.Orderings.pluginOrdering;

@Slf4j
@Configuration
public class ApiRightsConfig {
    @Configuration
    @ConditionalOnProperty(prefix = "yun.api.rights", name = "update", havingValue = "false")
    static class OnlyInitApiGroup{
        @Autowired
        public OnlyInitApiGroup(List<Docket> dockets) {
            nullToEmptyList(dockets).forEach(docket -> {
                ApiInfo apiInfo = (ApiInfo) ReflectUtil.getFieldValue(docket, "apiInfo");
                ApiRightsServiceImpl.groups.add(new NameValue<>(apiInfo.getTitle(), docket.getGroupName()));
            });
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "yun.api.rights", name = "update", havingValue = "true")
    @Conditional(SpringIntegrationPluginNotPresentInClassPathCondition.class)
    static class ApiRightsInit extends AbstractDocumentationPluginsBootstrapper implements SmartLifecycle {
        private final AtomicBoolean initialized = new AtomicBoolean(false);
        private final  List<Docket> dockets;
        private final  List<RequestMappingInfoHandlerMapping> handlerMappings;
        private final  JdbcTemplate jdbcTemplate;

        @Autowired
        public ApiRightsInit(
                DocumentationPluginsManager documentationPluginsManager,
                List<RequestHandlerProvider> handlerProviders,
                ApiDocumentationScanner resourceListing,
                TypeResolver typeResolver,
                Defaults defaults,
                PathProvider pathProvider,

                List<Docket> dockets,
                List<RequestMappingInfoHandlerMapping> handlerMappings,
                JdbcTemplate jdbcTemplate) {
            super(documentationPluginsManager, handlerProviders, new DocumentationCache(),
                    resourceListing, defaults, typeResolver, pathProvider);
            this.dockets = dockets;
            this.handlerMappings = handlerMappings;
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        protected void bootstrapDocumentationPlugins() {
            List<DocumentationPlugin> plugins = getDocumentationPluginsManager().documentationPlugins()
                    .stream()
                    .sorted(pluginOrdering())
                    .collect(toList());
            for (DocumentationPlugin each : plugins) {
                scanDocumentation(buildContext(each));
            }
        }

        @Override
        public void start() {
            if (initialized.compareAndSet(false, true)) {
                init();
                getScanned().clear();
            }
        }

        @Override
        public void stop() {
            initialized.getAndSet(false);
        }

        @Override
        public boolean isRunning() {
            return initialized.get();
        }

        private void init() {
            RequestMappingHandlerMapping handlerMapping = null;
            if (CollectionUtil.isNotEmpty(handlerMappings)) {
                for (RequestMappingInfoHandlerMapping mapping : handlerMappings) {
                    if (RequestMappingHandlerMapping.class == mapping.getClass()) {
                        handlerMapping = (RequestMappingHandlerMapping) mapping;
                        break;
                    }
                }
            }

            if (handlerMapping != null) {
                Map<String, String> urlAuthorizationMap = BuilderDefaults.nullToEmptyMap(handlerMapping.getHandlerMethods()).entrySet().stream()
                        .filter(entry -> entry.getValue().getMethod().isAnnotationPresent(PreAuthorize.class))
                        .map(toUrlAuthorizeMap()).collect(Collectors.toMap(KV::getKey, KV::getValue));
                bootstrapDocumentationPlugins();
                LocalDateTime now = LocalDateTime.now();
                nullToEmptyList(dockets).forEach(docket -> {
                    ApiInfo apiInfo = (ApiInfo) ReflectUtil.getFieldValue(docket, "apiInfo");
                    ApiRightsServiceImpl.groups.add(new NameValue<>(apiInfo.getTitle(), docket.getGroupName()));
                    String groupName = Optional.ofNullable(docket.getGroupName()).orElse(Docket.DEFAULT_GROUP_NAME);
                    Documentation documentation = getScanned().documentationByGroup(groupName);
                    if (documentation != null) {
                        List<ApiRightsDO> apiRights = nullToEmptyMap(documentation.getApiListings()).values().stream()
                                .flatMap(Collection::stream)
                                .map(apiListing -> {
                                    String tag = apiListing.getTags().stream().findFirst().orElseGet(() -> new Tag("", "")).getName();
                                    return nullToEmptyList(apiListing.getApis()).stream().map(e -> {
                                        Operation operation = e.getOperations().get(0);
                                        String url = "{" + operation.getMethod() + " " + e.getPath() + "}";
                                        return new ApiRightsDO(groupName, tag, url, urlAuthorizationMap.getOrDefault(url, ""), operation.getSummary(), now);
                                    }).collect(toList());
                                })
                                .flatMap(Collection::stream)
                                .collect(toList());
                        try {
                            updateApiRights(apiRights, jdbcTemplate);
                        } catch (SQLException e) {
                            log.error("*** update API access rights into database failed ***", e);
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        private void updateApiRights(List<ApiRightsDO> apiRights, JdbcTemplate jdbcTemplate) throws SQLException {
            if (CollectionUtil.isEmpty(apiRights)) {
                return;
            }
            List<Object[]> batchArgs = apiRights.stream()
                    .map(e -> new Object[]{e.getGroup(), e.getTag(), e.getUrl(), e.getAuthorization(), e.getDescription(), e.getCreateTime()}).collect(toList());

            TransactionSynchronizationManager.initSynchronization();
            Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
            connection.setAutoCommit(false);
            try {
                jdbcTemplate.update("delete from t_sys_api_rights");
                jdbcTemplate.batchUpdate("insert into t_sys_api_rights (api_group, api_tag, api_url, api_authorization, api_description, api_create_time) values (?, ?, ?, ?, ?, ?)", batchArgs);
                connection.commit();
                log.info("*** update API access rights to database successfully ***");
            } catch (Exception e) {
                connection.rollback();
                log.error("*** update API access rights into database failed ***");
                e.printStackTrace();
            } finally {
                try {
                    TransactionSynchronizationManager.clearSynchronization();
                } finally {
                    connection.setAutoCommit(true);
                }
            }
        }

        private Function<Map.Entry<RequestMappingInfo, HandlerMethod>, KV<String, String>> toUrlAuthorizeMap() {
            return input -> {
                PreAuthorize annotation = input.getValue().getMethod().getAnnotation(PreAuthorize.class);
                return new KV<>(input.getKey().toString(), extractUrlRightsFrom(annotation.value()));
            };
        }

        private final static Pattern pattern = Pattern.compile("@el.check\\('(.+)'\\)");

        private static String extractUrlRightsFrom(String text) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String group = matcher.group(1);
                group = group.replaceAll("'\\s*,\\s*'", ",");
                return StringUtils.hasText(group) ? group : "";
            }
            return "";
        }

        class KV<k, v> {
            k key;
            v value;

            KV(k key, v value) {
                this.key = key;
                this.value = value;
            }

            public k getKey() {
                return key;
            }

            public void setKey(k key) {
                this.key = key;
            }

            public v getValue() {
                return value;
            }

            public void setValue(v value) {
                this.value = value;
            }
        }
    }
}
