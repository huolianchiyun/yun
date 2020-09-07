package com.zhangbin.yun.yunrights.modules.common.config.filter;

import com.github.pagehelper.PageHelper;
import com.zhangbin.yun.yunrights.modules.rights.datarights.RuleManager;
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
        // 当请求线程处理业务逻辑后，清空PageHelper分页缓存，防止程序异常时，该线程没有消费掉page参数，而该线程再次分配来select sql，将会使用上次没有消费掉的page参数
        PageHelper.clearPage();
        // 清空RuleManager当前线程规则缓存，防止使用者没有手动清除，导致内存泄漏
        RuleManager.clearRuleMap();
    }

    @Override
    public void destroy() {

    }
}
