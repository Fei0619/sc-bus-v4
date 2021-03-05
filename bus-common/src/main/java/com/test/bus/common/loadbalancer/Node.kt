package com.test.bus.common.loadbalancer

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author 费世程
 * @date 2021/3/4 9:47
 */
class Node<S>(
    /**
     * 服务实体
     */
    val server: Server<S>,
    /**
     * 配置文件中指定的该后端的权重
     */
    val weight: Int) : Comparable<Node<S>> {

  /*
  AtomicInteger可保证多线程中原子操作，适用于在多线程中的计算
   */
  /**
   * 后端有效权重 </br>
   * 此后有新的请求过来时，在选取后端的过程中会逐渐增加，当后端发生错误时降低其权重
   */
  val effectiveWeight = AtomicInteger(weight)
  /**
   * 后端目前权重
   */
  val currentWeight = AtomicInteger(0)
  val available = AtomicBoolean(true)

  fun reset() {
    effectiveWeight.set(weight)
    currentWeight.set(0)
    available.set(true)
  }

  override fun compareTo(other: Node<S>): Int {
    return currentWeight.get() - other.currentWeight.get()
  }

}
