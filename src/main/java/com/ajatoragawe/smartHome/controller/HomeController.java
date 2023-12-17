package com.ajatoragawe.smartHome.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
    @MessageMapping("/send/message/{topic}")
    @SendTo("/topic/{topic}")
    public String sendMessage(String message) {
        System.out.println("received from MQTT : "+message);
        return message;
    }
}

