package com.example.shop.controllers.authorize;

import com.example.shop.model.DTO.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000" )
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/str")
    public ResponseEntity<String> test() {

        return ResponseEntity.ok("TEST");
    }

}
