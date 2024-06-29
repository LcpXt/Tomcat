package com.colin.servlet.annotation;

import java.lang.annotation.*;

/**
 * 2024年06月28日16:18
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebFilter {
    String value();
}
