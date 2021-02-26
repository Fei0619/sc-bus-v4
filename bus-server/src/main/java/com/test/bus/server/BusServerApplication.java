package com.test.bus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 费世程
 * @date 2021/2/26 16:30
 */
@EnableDiscoveryClient
@SpringBootApplication
public class BusServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BusServerApplication.class, args);
  }

}
