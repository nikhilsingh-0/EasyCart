package com.ecom.user.service;

import com.ecom.user.dto.AuthRequest;
import com.ecom.user.dto.UserRequest;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;


    public ResponseEntity<String> login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if(authentication!=null && authentication.isAuthenticated()){
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("User not found for email "+request.getEmail()));
            return ResponseEntity.ok(jwtService.generateToken(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username or password is not correct");
    }

    public ResponseEntity<String> register(UserRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already Exist. Please login");
        }
        User user = userService.addUser(request);
        if (user.getId()>0){
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtService.generateToken(user));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating user");
    }
}
