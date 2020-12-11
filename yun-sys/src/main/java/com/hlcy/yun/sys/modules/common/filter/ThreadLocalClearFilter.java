package com.hlcy.yun.sys.modules.common.filter;

import com.hlcy.yun.sys.modules.rights.datarights.rule.RuleManager;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 线程本地变量统一清除过滤器
 */
@Component
@WebFilter(urlPatterns = "/*")
public class ThreadLocalClearFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        // 清空RuleManager当前线程规则缓存，防止使用者没有手动清除，导致内存泄漏
        RuleManager.clearRuleMap();
    }

    @Override
    public void destroy() {

    }
}
