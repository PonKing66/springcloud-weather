package com.hpg.springcloud.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ponking
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WeatherCollectionEurekaFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherCollectionEurekaFeignApplication.class, args);
    }

}
