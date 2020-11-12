package com.yun.common.filter;

import com.github.pagehelper.PageHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Component
@ConditionalOnClass(PageHelper.class)
@WebFilter(urlPatterns = "/*")
public class PageHelperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        // 当请求线程处理业务逻辑后，清空PageHelper分页缓存，防止程序异常时，该线程没有消费掉page参数，而该线程再次分配来select sql，将会使用上次没有消费掉的page参数
        PageHelper.clearPage();
    }
}
