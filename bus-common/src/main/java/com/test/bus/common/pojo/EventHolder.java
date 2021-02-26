package com.test.bus.common.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 费世程
 * @date 2021/2/26 16:28
 */
public class EventHolder {

  private final List<EventMessage<?>> messages = new ArrayList<>();

  private EventHolder() {
  }

  public static EventHolder create() {
    return new EventHolder();
  }


}
