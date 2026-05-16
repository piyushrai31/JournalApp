package com.devscircle.journalApp.controller;

import com.devscircle.journalApp.entity.User;
import com.devscircle.journalApp.service.UserDetailsServiceImpl;
import com.devscircle.journalApp.service.UserService;
import com.devscircle.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user){
        boolean newUser = userService.createNewUser(user);
       return newUser? new ResponseEntity<>(HttpStatus.OK): new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    @PostMapping("/login")
//    public String login(@RequestBody User user){
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
//
////            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
//            return jwtUtil.generateToken(user.getUserName());
//        } catch (Exception e) {
//            log.error("Exception in login " +  e);
//            return null;
//        }
//    }

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {

    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUserName(),
                        user.getPassword()
                )
        );

        String token = jwtUtil.generateToken(user.getUserName());

        return ResponseEntity.ok(token);
    } catch (AuthenticationException e) {
        log.error("Authentication Exception: ", e);
        throw new RuntimeException(e);
    }
}

}
