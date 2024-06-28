package com.colin.servlet.annotation;

import java.lang.annotation.*;

/**
 * 2024年06月27日18:50
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebListener {
}
