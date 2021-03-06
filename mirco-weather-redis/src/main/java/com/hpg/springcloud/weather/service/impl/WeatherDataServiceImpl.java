package com.hpg.springcloud.weather.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpg.springcloud.weather.service.WeatherDataService;
import com.hpg.springcloud.weather.vo.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Ponking
 * @ClassName WeatherDataServiceImpl
 * @date 2020/5/8--16:34
 **/
@Component
@Slf4j
public class WeatherDataServiceImpl implements WeatherDataService {

    private static final String WEATHER_URI = "http://wthrcdn.etouch.cn/weather_mini?";

    /**
     * 过期时间
     */
    private static final long TIME_OUT  = 1800L;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public WeatherResponse getDataByCityId(String cityId) {
        String uri = WEATHER_URI+"citykey="+cityId;
        return doGetWeather(uri);
    }

    @Override
    public WeatherResponse getDataByCityName(String cityName) {
        String uri = WEATHER_URI+"city="+cityName;
        return doGetWeather(uri);
    }


    /**
     * 先查缓存,缓存有的,从缓存拿取数据;否则,从服务器取数据,然后存储缓存区
     * @param uri
     * @return
     */
    private WeatherResponse doGetWeather(String uri){


        String key = uri;
        WeatherResponse weatherResponse = null;
        String responseBody = null;
        ObjectMapper mapper = new ObjectMapper();

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //查缓存
        if(stringRedisTemplate.hasKey(key)){
            log.info("Redis have data...");
            responseBody = ops.get(key);
        }else{
            //缓存没有
            log.info("Redis don't have data...");
            ResponseEntity<String> responseString = restTemplate.getForEntity(uri,String.class);
            if(responseString.getStatusCodeValue() == 200){
                responseBody = responseString.getBody();
            }

            //数据写入缓存
            ops.set(key,responseBody,TIME_OUT, TimeUnit.SECONDS);
        }

        try {
            weatherResponse = mapper.readValue(responseBody,WeatherResponse.class);
        } catch (JsonProcessingException e) {
            log.info("获取天气信息失败...");
        }

        return weatherResponse;
    }
}
