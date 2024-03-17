package com.example.livechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController  {

    @GetMapping("/")
    public String mainPage() {return "main";}

    @GetMapping("/login")
    public String loginPage() {return "login";}

    @GetMapping("/join")
    public String joinPage() {return "join";}

    @GetMapping("/menu")
    public String menuPage() {
        return "menu";
    }

    @GetMapping("/privatechat")
    public String privateChatPage() {return "privatechatroom";}
}
