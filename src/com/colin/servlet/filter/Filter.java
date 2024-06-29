package com.colin.servlet.filter;

import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.HttpServletResponse;

import java.io.IOException;

/**
 * 2024年06月28日16:19
 */
public interface Filter {
    default void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

    }
}
