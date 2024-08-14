package org.example.springsecuritydemo.controller;

import lombok.AllArgsConstructor;
import org.example.springsecuritydemo.configuration.JwtGenerator;
import org.example.springsecuritydemo.dto.LoginDto;
import org.example.springsecuritydemo.dto.RegisterDto;
import org.example.springsecuritydemo.dto.ResponseDto;
import org.example.springsecuritydemo.model.Role;
import org.example.springsecuritydemo.model.User;
import org.example.springsecuritydemo.repository.RoleRepository;
import org.example.springsecuritydemo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username Is Taken", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User Registered", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new ResponseDto(token), HttpStatus.OK);
    }

}
