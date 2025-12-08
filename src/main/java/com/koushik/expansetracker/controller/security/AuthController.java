package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.*;
import com.koushik.expansetracker.service.security.LoginService;
import com.koushik.expansetracker.service.security.SignupService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final SignupService signupService;

    public AuthController(LoginService loginService, SignupService signupService) {
        this.loginService = loginService;
        this.signupService = signupService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(signupService.signup(request));
    }
}
