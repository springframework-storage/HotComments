package com.naver.hackday.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class HealthCheck {

    @GetMapping(value = "/empathy/monitor/l7check")
    public String healthCheck(HttpServletResponse response) {
        response.setStatus(HttpStatus.OK.value());
        return "i'm running";
    }

}
