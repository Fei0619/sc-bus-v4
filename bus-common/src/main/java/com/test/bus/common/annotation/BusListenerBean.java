package com.test.bus.common.annotation;

import java.lang.annotation.*;

/**
 * @author 费世程
 * @date 2021/2/26 16:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface BusListenerBean {
}
