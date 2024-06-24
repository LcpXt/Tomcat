package com.colin.servlet.annotation;

import java.lang.annotation.*;

/**
 * 2024年06月24日16:45
 * 先实现基本的功能
 * 最后可以加一个loadstartup
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet {
    String value() default "";
}
