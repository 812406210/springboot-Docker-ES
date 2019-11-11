package com.elk.log.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "api/v1/log")
public class LogController {

    private static final ThreadPoolExecutor paralleExecutor=new ThreadPoolExecutor(5, 50, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());


    private final Logger logger = LoggerFactory.getLogger(LogController.class);

    @RequestMapping("/hello")
    public String HelloWorld(){
        return "index";
    }


    @RequestMapping("/test")
    public Object test(HttpServletRequest request){
        for (int i = 0; i <10 ; i++) {
            CompletableFuture<Void> cf= CompletableFuture.runAsync(()->{
                System.out.println("线程运行.....");
            }, paralleExecutor);
        }
        logger.info("你好啊e");
        logger.warn("This is a warn message!");
        logger.error("This is error message!");

        return  ResponseEntity.ok("server被调用了！:"+request.getServerPort());
    }

}
