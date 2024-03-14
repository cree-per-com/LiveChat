package com.example.livechat.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController  {

    @GetMapping("/")
    public String mainPage() {return "main";}

    @GetMapping("/login")
    public String LoginPage() {return "login";}

    @GetMapping("/join")
    public String JoinPage() {return "join";}

    @GetMapping("/menu")
    public String menuPage() {
        return "menu";
    }
}
