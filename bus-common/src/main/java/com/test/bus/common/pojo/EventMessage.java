package com.test.bus.common.pojo;


import lombok.*;

/**
 * @author 费世程
 * @date 2021/2/26 11:32
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage<T> {
  /**
   * 事件id
   */
  private String eventId;
  /**
   * 所属平台
   */
  private String platform;
  /**
   * 事件主题
   */
  private String topic;
  /**
   * 事件头
   */
  private EventHeaders headers = EventHeaders.create();
  /**
   * 消息体
   */
  private T payload;
  /**
   * 延迟时间
   */
  private long delayMillis = -1L;
  /**
   * 事件产生时间戳
   */
  private long timestamp = System.currentTimeMillis();

}
