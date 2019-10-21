package com.elk.log.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    @RequestMapping("/hello")
    public String HelloWorld(){
        return "index";
    }

}
