package com.test.bus.api.internal.deliver;

import com.fasterxml.jackson.databind.JavaType;
import com.test.bus.api.internal.pojo.EventListenerContext;
import com.test.bus.common.annotation.BusEventListener;
import com.test.bus.common.annotation.BusListenerBean;
import com.test.bus.common.util.ConditionMatcher;
import com.test.share.common.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author 费世程
 * @date 2021/3/2 11:07
 */
public class ListenerFactory implements InitializingBean {

  private static final Map<String, List<EventListenerContext>> EVENT_HANDLER_MAPPING = new HashMap<>();
  private final ApplicationContext context;
  private final Logger log = LoggerFactory.getLogger(ListenerFactory.class);

  public ListenerFactory(ApplicationContext context) {
    this.context = context;
  }

  public static List<EventListenerContext> getEventListenerContext(String topic) {
    return EVENT_HANDLER_MAPPING.get(topic);
  }

  @Override
  public void afterPropertiesSet() {
    init();
  }

  private void init() {
    Map<String, Object> beanMapping = context.getBeansWithAnnotation(BusListenerBean.class);
    Collection<Object> beans = beanMapping.values();
    for (Object bean : beans) {
      Class<?> clazz = bean.getClass();
      Method[] methods = clazz.getMethods();
      for (Method method : methods) {
        BusEventListener busEventListener = method.getAnnotation(BusEventListener.class);
        if (busEventListener != null) {
          String topic = busEventListener.topic();
          if (StringUtils.isBlank(topic)) {
            String className = clazz.getName();
            String methodName = method.getName();
            log.error("{}#{} @BusEventListener未指定topic...", className, methodName);
            continue;
          }
          Parameter[] parameters = method.getParameters();
          if (parameters != null && parameters.length == 1) {
            Parameter parameter = parameters[0];
            JavaType javaType = JsonUtils.INSTANCE.getJavaType(parameter.getParameterizedType());
            EventListenerContext context = new EventListenerContext(javaType, method, bean, ConditionMatcher.parseConditions(busEventListener.condition()));
            List<EventListenerContext> contests = EVENT_HANDLER_MAPPING.computeIfAbsent(topic, key -> new ArrayList<>());
            contests.add(context);
          } else {
            String className = clazz.getName();
            String methodName = method.getName();
            log.error("{}#{} @BusEventListener参数列表过长...", className, methodName);
          }
        }
      }
    }

  }

}
