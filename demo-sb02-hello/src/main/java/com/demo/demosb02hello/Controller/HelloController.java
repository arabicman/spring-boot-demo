package com.demo.demosb02hello.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(@RequestParam(required = false, name = "name")String name){
        if(name == null){
            name = "World";
        }
        return "Hello "+name+"!";
    }
}
