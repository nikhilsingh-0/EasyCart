package com.example.producer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
@RestController
public class ProducerController {


    ArrayList<Info> list = new ArrayList<>();

    @GetMapping("info")
    public ArrayList<Info> getInfo(){
        list.add(new Info(1,"Producer"));
        return list;
    }

    @PostMapping("info")
    public ArrayList<Info> postInfo(@RequestBody Info info){
        list.add(info);
        return list;
    }
}
