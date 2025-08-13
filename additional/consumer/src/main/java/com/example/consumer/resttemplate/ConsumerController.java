package com.example.consumer.resttemplate;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rest-template")
public class ConsumerController {

    @GetMapping("/info")
    public ArrayList<Info> getInfo(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ArrayList<Info>> result = restTemplate.exchange("http://localhost:8001/info", HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Info>>() {});
        return result.getBody();
    }

    @PostMapping("/info")
    public ArrayList<Info> postInfo(){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Info> entity = new HttpEntity<>(new Info(2,"from Consumer"));
        ResponseEntity<ArrayList<Info>> result = restTemplate.exchange("http://localhost:8001/info", HttpMethod.POST, entity, new ParameterizedTypeReference<ArrayList<Info>>() {});
        return result.getBody();
    }
}
