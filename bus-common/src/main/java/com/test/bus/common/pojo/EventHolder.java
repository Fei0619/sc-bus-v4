package com.test.bus.common.pojo;

import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 费世程
 * @date 2021/2/26 16:28
 */
public class EventHolder {

  private final List<EventMessage<?>> messages = new ArrayList<>();
  private int cursor = -1;

  private EventHolder() {
  }

  @NonNull
  public static EventHolder create() {
    return new EventHolder();
  }

  @NonNull
  private static EventHolder create(@NonNull String topic, @NonNull Object payload) {
    EventHolder holder = new EventHolder();
    EventMessage<Object> eventMessage = new EventMessage<>(topic, payload);
    holder.messages.add(eventMessage);
    holder.cursor = 0;
    return holder;
  }

  @NonNull
  private static EventHolder create(@NonNull EventPayload payload) {
    return create(payload.getTopic(), payload);
  }

  // -------------------------------------------- 追加事件 -------------------------------------------- //
  @NonNull
  public EventHolder then(@NonNull String topic, @NonNull Object payload) {
    final EventMessage<Object> eventMessage = new EventMessage<>(topic, payload);
    return then(eventMessage);
  }

  @NonNull
  public EventHolder then(@NonNull EventPayload payload) {
    return then(payload.getTopic(), payload);
  }

  @NonNull
  public EventHolder then(@NonNull EventMessage<?> eventMessage) {
    this.messages.add(eventMessage);
    this.cursor++;
    return this;
  }

  // --------------------------------------- 设置事件推送条件 --------------------------------------- //
  // todo

  // --------------------------------------- 设置事件延迟时间 --------------------------------------- //
  @NonNull
  public EventHolder delayMillis(@NonNull Long delayMillis) {
    EventMessage<?> eventMessage = this.messages.get(cursor);
    eventMessage.setDelayMillis(delayMillis);
    return this;
  }

  @NonNull
  public EventHolder delayMillis(@NonNull Duration duration) {
    EventMessage<?> eventMessage = this.messages.get(cursor);
    eventMessage.setDelayMillis(duration.toMillis());
    return this;
  }
}
