package com.example.livechat.controller;

import com.example.livechat.dao.JoinDTO;
import com.example.livechat.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {this.joinService=joinService;}

    @PostMapping("/joinProc")
    public ResponseEntity<String> joinProc(@RequestBody JoinDTO dto) {
        joinService.joinProc(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
