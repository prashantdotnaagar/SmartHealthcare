package com.SmartHealthcare.controller;

import com.SmartHealthcare.dto.LoginRequest;
import com.SmartHealthcare.dto.request.admin.RegisterRequestAdmin;
import com.SmartHealthcare.dto.request.doctor.RegisterRequestDoctor;
import com.SmartHealthcare.dto.request.patient.RegisterRequestPatient;
import com.SmartHealthcare.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SmartHealthcare.Impl.RegisterImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;


    @Autowired
    private RegisterImpl register;

    @PostMapping("/doctor/register")
    ResponseEntity<String> register(@RequestBody RegisterRequestDoctor request) {
        register.registerDoctor(request);
        return ResponseEntity.ok("Doctor Registered successfully");
    }

    @PostMapping("/patient/register")
    ResponseEntity<String> register(@RequestBody RegisterRequestPatient request) {
        register.registerPatient(request);
        return ResponseEntity.ok("Patient Registered successfully");
    }

    @PostMapping("/admin/register")
    ResponseEntity<String> register(@RequestBody RegisterRequestAdmin request) {
        register.registerAdmin(request);
        return ResponseEntity.ok("Patient Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getUserPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        System.out.println(cookie);
        return ResponseEntity.ok("Login successful. JWT stored in cookie.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String jwt = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    System.out.println(jwt);
                    break;
                }
            }
        }
        if (jwt == null || jwtService.isTokenExpired(jwt)) {
            return ResponseEntity.status(401).body("You are not logged in");
        }
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
