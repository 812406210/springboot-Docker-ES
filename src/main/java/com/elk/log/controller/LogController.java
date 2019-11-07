package com.elk.log.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/log")
public class LogController {

    @RequestMapping("/hello")
    public String HelloWorld(){
        return "index";
    }

}
