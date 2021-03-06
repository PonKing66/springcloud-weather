package com.hpg.springcloud.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


/**
 * @author Ponking
 * @ClassName EurekaServerApplication
 * @date 2020/5/9--18:09
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class EurekaClientZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientZuulApplication.class,args);
    }
}
