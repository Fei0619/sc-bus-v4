package com.test.bus.server.conf

import com.test.bus.common.loadbalancer.LoadBalancer
import com.test.bus.common.loadbalancer.PollingLoadBalancer
import com.test.bus.server.properties.RabbitProperties
import com.test.share.common.util.ThreadPools
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import reactor.rabbitmq.Sender
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2021/3/4 14:28
 */
@Configuration
open class RabbitLoadBalancerConfig(private val rabbitProperties: RabbitProperties) {

  private val log = LoggerFactory.getLogger(RabbitLoadBalancerConfig::class.java)

  fun rabbitLoadbalancer(): LoadBalancer<Sender> {
    val loadbalancer = PollingLoadBalancer<Sender>()
    loadbalancer.nodeUnavailableCallback = { node ->
      val nodeId = node.server.id
      log.error("RabbitMQ sender节点：{}异常，已从服务列表移除...", nodeId)
      val resetDelaySeconds = rabbitProperties.resetUnavailableNodeDelaySeconds
      if (resetDelaySeconds > 0) {
        ThreadPools.commonSchedule.schedule({
          log.info("重新尝试将不可用的RabbitMQ Sender节点加入轮询服务列表,nodeId -> {}", nodeId)
          loadbalancer.resetServer(node.server)
        }, resetDelaySeconds, TimeUnit.SECONDS)
      }
      synchronized(nodeId) {
        //todo 发送节点不可用告警邮件
      }
    }
    loadbalancer.serverUnavailableCallback = {

    }
    val servers = rabbitProperties.servers
    for (server in servers) {
      val host = server.host
      val port = server.port
      val weight = server.weight

    }


    return loadbalancer
  }

}
