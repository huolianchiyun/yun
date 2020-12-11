package com.hlcy.yun.admincenter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yun/vm")
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello(){
        return "hello word";
    }
}
