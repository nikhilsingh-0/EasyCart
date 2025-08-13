package com.example.consumer.openfeign;

import com.example.consumer.resttemplate.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/feign")
public class OpenFeignController {

    @Autowired
    ProducerFeignClient producerFeignClient;

    @GetMapping("/info")
    public ArrayList<Info> getInfo(){
        return producerFeignClient.getInfo();
    }

    @PostMapping("/info")
    public ArrayList<Info> postInfo(){
        return producerFeignClient.postInfo(new Info(2,"From Consumer from feign"));
    }
}
