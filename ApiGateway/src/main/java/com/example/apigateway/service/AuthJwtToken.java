package com.example.apigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthJwtToken {

    private final RestTemplate restTemplate;
    //private final RestTemplate restTemplate;


    public AuthJwtToken(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public boolean verifyToken(String token){
        System.out.println("E verifichiamo sto token.....");
        return true;
    }
}
