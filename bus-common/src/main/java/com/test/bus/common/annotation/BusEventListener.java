package com.test.bus.common.annotation;

import java.lang.annotation.*;

/**
 * @author 费世程
 * @date 2021/2/26 16:45
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface BusEventListener {

  /**
   * 监听主题
   */
  String topic();

  /**
   * 监听条件
   */
  String condition() default "";

}
