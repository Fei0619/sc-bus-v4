package com.test.bus.common.loadbalancer

/**
 * 负载均衡器接口
 * @author 费世程
 * @date 2021/3/4 9:47
 */
interface LoadBalancer<S> {

  /**
   * 注册一个服务节点
   */
  fun register(node: Node<S>)

  /**
   * 选取一个服务节点
   */
  fun select(): Server<S>?

  /**
   * 尝试获取一个不为空的服务节点，直到重试次数达[retry]
   */
  fun select(retry: Int): Server<S>? {
    var server: Server<S>? = null
    var tempRetry = 0
    while (server == null && tempRetry++ < retry) {
      server = select()
    }
    return server
  }

  /**
   * 服务调用成功后回执方法
   */
  fun onInvokeSuccess(server: Server<S>)

  /**
   * 服务调用失败后回执方法
   */
  fun onInvokeFail(server: Server<S>)

  /**
   * 将服务标记为不可用
   */
  fun unavailable(server: Server<S>)

  /**
   * 重置节点信息
   */
  fun resetServer(server: Server<S>)

}
