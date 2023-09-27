package com.example.errorsrepo;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.example.errorsrepo.error.Error;
import com.example.errorsrepo.error.ErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MainController {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ErrorRepository errorRepo;
    @GetMapping("/reportPage")
    public ModelAndView showReportPage(){
        ModelAndView m_v = new ModelAndView();
        m_v.setViewName("index");
        return m_v;
    }

    @GetMapping("/getAllErrors")
    public ResponseEntity<String> getAllError(){
        return ResponseEntity.ok(errorRepo.findAll().toString());
    }

    @GetMapping("/getErrorById")
    public ResponseEntity<String> getAllError(@RequestParam("id") Integer identifier){
        return ResponseEntity.ok(errorRepo.findById(identifier).toString());
    }

    @GetMapping("/getErrorByStatusCode")
    public ResponseEntity<String> getErrorByStatusCode(@RequestParam("statusCode") Integer statusCode){
        return ResponseEntity.ok(errorRepo.findByErrorCode(statusCode).toString());
    }

    @GetMapping("/getErrorsByDate")
    public ResponseEntity<String> getErrorByDate(@RequestParam("date")String s_data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            LocalDate date_to_pass = LocalDate.parse(s_data,formatter);
            return ResponseEntity.ok(errorRepo.findByDate(date_to_pass).toString());
        }catch (Exception exp){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.getMessage());
        }

    }

    @GetMapping("/errorBetween")
    public ResponseEntity<String> findErrorBetween(@RequestParam("date1")String date1,
                                                   @RequestParam("date2")String date2){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            LocalDate date1_p = LocalDate.parse(date1,formatter);
            LocalDate date2_p = LocalDate.parse(date2,formatter);
            return ResponseEntity.ok(errorRepo.findByDateIsBetween(date1_p,date2_p).toString());
        }catch (Exception exp){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.getMessage());
        }

    }
    @PostMapping("/insertError")
    public ResponseEntity<String> insertError(@RequestParam("description") String desc,
                                              @RequestParam("statusCode") Integer statusCode,
                                              @RequestParam("fromReq") String fromReq){
        Error e = new Error();
        try{
            e.setErrorCode(statusCode);
            e.setDescription(desc);
            e.setFrom(fromReq);
            e.setDate(LocalDate.now());
            errorRepo.save(e);
            return ResponseEntity.status(HttpStatus.OK).body("Errore riportato");
        }catch (Exception except){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(except.getMessage());
        }
    }

}
