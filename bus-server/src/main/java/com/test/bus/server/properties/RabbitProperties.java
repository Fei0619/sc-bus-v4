package com.test.bus.server.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 费世程
 * @date 2021/3/4 16:25
 */
/*
   当配置发生变更的时候可以在不重启应用的前提下完成bean中相关属性的刷新
 */
@RefreshScope
@Component
@ConfigurationProperties(prefix = "sc.bus.rabbit")
@Getter
@Setter
public class RabbitProperties {

  @NonNull
  private String virtualHost = "/";
  @NonNull
  private String username = "";
  @NonNull
  private String password = "";

  private List<RabbitServer> servers = new ArrayList<>();
  /**
   * 当某个节点被标记为不可用状态时，会在等待一段时间后重置该节点将其加入负载均衡服务列表
   * 如果该值小于1，则永远不会重置该节点的状态
   */
  private long resetUnavailableNodeDelaySeconds = 600L;
  /**
   * 是否开启消息持久化
   */
  private boolean enableMessagePersistence = false;

  private String exchangeName = "sc.simpleExchange";
  private String delayExchangeName = "sc.delayExchange";
  private String eventMessageQueue = "sc.eventMessage";
  private String delayMessageQueue = "scc.delayMessage";

  @Getter
  @Setter
  public static class RabbitServer {
    @NonNull
    private String host = "127.0.0.1";
    private int port = 5672;
    private int weight = 10;
  }

}
