package com.example.consumeapi;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "api-service",url="https://restcountries.eu")
public interface apiServiceProxy extends apiService {

}
