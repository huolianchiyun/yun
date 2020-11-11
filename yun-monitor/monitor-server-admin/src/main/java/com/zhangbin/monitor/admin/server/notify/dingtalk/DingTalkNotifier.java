package com.zhangbin.monitor.admin.server.notify.dingtalk;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DingTalkNotifier extends AbstractStatusChangeNotifier {
    private static final String DEFAULT_MESSAGE = "*#{instance.registration.name}* (#{instance.id}) is *#{event.statusInfo.status}*";
    private static final SpelExpressionParser parser = new SpelExpressionParser();
    private RestTemplate restTemplate = new RestTemplate();
    private Expression message;
    private HttpHeaders headers;
    @Autowired
    private DingtalkProperties properties;

    public DingTalkNotifier(InstanceRepository repository) {
        super(repository);
        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> restTemplate.postForEntity(properties.getWebhookToken(), createRequest(event, instance), String.class));
    }

    private HttpEntity<String> createRequest(InstanceEvent event, Instance instance) {
        // 按策略选择用哪种消息类型
        return new HttpEntity<>(Objects.requireNonNull(MessageFactory
                .getBuilderBy(properties.getMsgtype()))
                .resource(properties.setContent(buildMsg(event, instance)))
                .build().toJsonString(), headers);
    }

    private String buildMsg(InstanceEvent event, Instance instance) {
        Map<String, Object> root = new HashMap<>();
        root.put("event", event);
        root.put("instance", instance);
        root.put("lastStatus", getLastStatus(event.getInstance()));
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return message.getValue(context, String.class);
    }
}
