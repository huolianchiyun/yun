package com.zhangbin.yun.yunrights.modules.common.config.filter;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * PageHelper清空新请求处理线程绑定的分页缓存
 */
@Component
@WebFilter(urlPatterns = "/*")
public class clearPageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        //清空PageHelper分页缓存
        PageHelper.clearPage();
    }

    @Override
    public void destroy() {

    }
}
