package com.example.consumer.openfeign;

import com.example.consumer.resttemplate.Info;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@FeignClient(name = "producer-service",url = "http://localhost:8001")
@Service
public interface ProducerFeignClient {


    @GetMapping("/info")
    public ArrayList<Info> getInfo();

    @PostMapping("/info")
    public ArrayList<Info> postInfo(@RequestBody Info info);
}
