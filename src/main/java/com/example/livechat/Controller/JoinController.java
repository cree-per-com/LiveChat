package com.example.livechat.Controller;

import com.example.livechat.DAO.JoinDTO;
import com.example.livechat.Service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {this.joinService=joinService;}

    @PostMapping("/joinProc")
    public ResponseEntity<String> JoinProc(@RequestBody JoinDTO dto) {
        joinService.joinProc(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
