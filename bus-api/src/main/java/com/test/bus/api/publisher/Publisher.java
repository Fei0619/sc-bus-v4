package com.test.bus.api.publisher;

import com.test.bus.common.pojo.message.EventHolder;
import com.test.bus.common.pojo.message.EventMessage;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author 费世程
 * @date 2021/2/26 16:55
 */
public interface Publisher<T> {

  /**
   * 发布单个事件
   */
  T publish(EventMessage<?> message);

  /**
   * 批量发布事件
   */
  T batchPublish(List<EventMessage<?>> messages);

  /*
   java8中提供了关键字[default]，接口中使用该关键字修饰的方法可以有自己的实现
   */
  @NonNull
  default T publish(@NonNull EventHolder holder) {
    List<EventMessage<?>> messages = holder.getMessages();
    if (messages.size() == 1) {
      return publish(messages.get(0));
    } else {
      return batchPublish(messages);
    }
  }

}
