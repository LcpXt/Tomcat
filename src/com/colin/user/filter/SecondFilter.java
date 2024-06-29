package com.colin.user.filter;

import com.colin.servlet.annotation.WebFilter;
import com.colin.servlet.filter.Filter;
import com.colin.servlet.filter.FilterChain;
import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.HttpServletResponse;

import java.io.IOException;

/**
 * 2024年06月29日上午11:43
 */
@WebFilter("/login")
public class SecondFilter implements Filter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("请求来到第2个过滤器");
        filterChain.doFilter(req, resp);
        System.out.println("响应返回到第2个过滤器");
    }
}
