package com.example.livechat.Controller;

import com.example.livechat.DAO.JoinDTO;
import com.example.livechat.Service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {this.joinService=joinService;}

    @PostMapping("/join")
    public String JoinProc(JoinDTO dto) {
        joinService.joinProc(dto);
        return "ok";
    }
}
