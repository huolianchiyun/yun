package com.hlcy.yun.monitor.admin.server.notify.config;

import com.hlcy.yun.monitor.admin.server.notify.dingtalk.DingTalkNotifier;
import com.hlcy.yun.monitor.admin.server.notify.dingtalk.DingtalkProperties;
import com.hlcy.yun.monitor.admin.server.notify.sms.SMSNotifier;
import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration.CompositeNotifierConfiguration;
import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration.NotifierTriggerConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.boot.admin.notify", name = "enable", havingValue = "true")
@AutoConfigureBefore({NotifierTriggerConfiguration.class, CompositeNotifierConfiguration.class})
public class NotifierConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.boot.admin.notify.dingtalk", name = {"webhook-token"})
    public DingTalkNotifier dingTalkNotifier(InstanceRepository repository) {
        return new DingTalkNotifier(repository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.boot.admin.notify.dingtalk", name = {"webhook-token"})
    @ConfigurationProperties(prefix = "spring.boot.admin.notify.dingtalk")
    public DingtalkProperties dingtalkProperties(InstanceRepository repository) {
        return new DingtalkProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.boot.admin.notify.sms")
    @ConditionalOnProperty(prefix = "spring.boot.admin.notify.sms", name = {"token"})
    public SMSNotifier dNotifier(InstanceRepository repository) {
        return new SMSNotifier(repository);
    }
}
