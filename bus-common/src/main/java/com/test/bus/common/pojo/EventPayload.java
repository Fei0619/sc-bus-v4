package com.test.bus.common.pojo;

import java.beans.Transient;

/**
 * @author 费世程
 * @date 2021/2/26 11:32
 */
public interface EventPayload {

  /**
   * 获取事件主题
   */
  @Transient
  String getTopic();

}
