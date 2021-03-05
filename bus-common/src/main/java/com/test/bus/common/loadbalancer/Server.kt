package com.test.bus.common.loadbalancer

/**
 * @author 费世程
 * @date 2021/3/4 9:48
 */
class Server<S>(
    /**
     * 服务唯一id
     */
    val id: String,
    /**
     * 服务实体
     */
    val server: S) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (this.javaClass != other?.javaClass) return false
    other as Server<*>
    if (this.id != other.id) return false
    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}
