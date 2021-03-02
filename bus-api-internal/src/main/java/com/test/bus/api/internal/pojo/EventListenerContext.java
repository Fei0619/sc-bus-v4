package com.test.bus.api.internal.pojo;

import com.fasterxml.jackson.databind.JavaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @author 费世程
 * @date 2021/3/2 11:12
 */
@Getter
@AllArgsConstructor
public class EventListenerContext {

  private JavaType javaType;
  private Method method;
  private Object instance;
  private List<Set<String>> conditionsGroup;

  public Object process(Object message) throws Exception {
    return this.method.invoke(this.instance, message);
  }

}
