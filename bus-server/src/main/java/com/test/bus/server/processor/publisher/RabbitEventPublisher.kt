package com.test.bus.server.processor.publisher

import com.test.bus.api.publisher.EventPublisher
import com.test.bus.common.pojo.message.EventMessage
import com.test.bus.common.pojo.result.PublishResult
import com.test.share.common.result.Res
import org.slf4j.LoggerFactory

/**
 * @author 费世程
 * @date 2021/3/4 9:44
 */
class RabbitEventPublisher : EventPublisher {


  private val log = LoggerFactory.getLogger(RabbitEventPublisher::class.java)

  override fun publish(message: EventMessage<*>?): Res<PublishResult> {
    TODO()
  }

  override fun batchPublish(messages: MutableList<EventMessage<*>>?): Res<PublishResult> {
    TODO()
  }
}
