package com.test.bus.common.loadbalancer

import java.util.concurrent.atomic.AtomicInteger

/**
 * 加权轮询负载均衡算法 </br>
 * 1. 基于权重的轮询
 * 2. 调用失败降低权重
 * 3. 连续调用失败移除服务
 *
 * @author 费世程
 * @date 2021/3/4 11:13
 */
class PollingLoadBalancer<S>() : LoadBalancer<S> {

  constructor(
      /**
       * 当某个节点失败次数过多时会将节点置为不可用状态并调用此函数
       * 可自定义此函数以实现节点不可用发送告警等功能，或在错误处理后重置此[Node]状态
       */
      nodeUnavailableCallback: (Node<S>) -> Unit,
      /**
       * 每当[select]返回的服务为空时则会调用此函数，这意味着当前所有的服务都被标记为不可用状态
       */
      serverUnavailableCallback: (AtomicInteger) -> Unit) : this() {
    this.nodeUnavailableCallback = nodeUnavailableCallback
    this.serverUnavailableCallback = serverUnavailableCallback
  }

  private val nodes = ArrayList<Node<S>>()
  /**
   * 服务不可用状态计数器
   */
  private val resetCounter = AtomicInteger(0)
  var nodeUnavailableCallback: (unavailableNode: Node<S>) -> Unit = {}
  var serverUnavailableCallback: (resetCount: AtomicInteger) -> Unit = {}

  override fun register(node: Node<S>) {
    synchronized(this) {
      this.nodes.add(node)
    }
  }

  override fun select(): Server<S>? {
    if (nodes.isEmpty()) {
      return null
    }
    if (nodes.size == 1) {
      return nodes[0].server
    }
    //过滤掉不可用的节点
    val availableNodes = nodes.filter { it.available.get() }
    if (availableNodes.isEmpty()) {
      //当前没有可用的服务了，此时重置所有节点的状态
      synchronized(this) {
        resetCounter.incrementAndGet()
        serverUnavailableCallback.invoke(resetCounter)
        nodes.forEach { it.reset() }
      }
      return null
    }
    var total = 0
    var nodeOfMaxWeight: Node<S>? = null
    for (node in availableNodes) {
      val effectiveWeight = node.effectiveWeight.get()
      node.currentWeight.addAndGet(effectiveWeight)
      total += effectiveWeight
      nodeOfMaxWeight = if (nodeOfMaxWeight == null) {
        node
      } else {
        if (node > nodeOfMaxWeight) node else nodeOfMaxWeight
      }
    }
    //轮询
    nodeOfMaxWeight!!.currentWeight.addAndGet(-total)
    return nodeOfMaxWeight.server
  }

  override fun onInvokeSuccess(server: Server<S>) {
    val serverId = server.id
    nodes.filter { it.server.id == serverId }
        .forEach { node ->
          node.effectiveWeight.updateAndGet { if (it < node.weight) it + 1 else it }
        }
  }

  override fun onInvokeFail(server: Server<S>) {
    val serverId = server.id
    nodes.filter { it.server.id == serverId }
        .forEach { node ->
          synchronized(serverId) {
            val effectiveWeight = node.effectiveWeight.decrementAndGet()
            if (effectiveWeight == 0 && node.available.getAndSet(false)) {
              nodeUnavailableCallback.invoke(node)
            }
          }
        }
  }

  override fun unavailable(server: Server<S>) {
    val serverId = server.id
    synchronized(serverId) {
      nodes.filter { it.server.id == serverId }
          .forEach { node ->
            if (node.available.getAndSet(false)) {
              nodeUnavailableCallback.invoke(node)
            }
          }
    }
  }

  override fun resetServer(server: Server<S>) {
    val serverId = server.id
    synchronized(serverId) {
      nodes.filter { it.server.id == serverId }
          .forEach { node ->
            node.reset()
          }
    }
  }
}
