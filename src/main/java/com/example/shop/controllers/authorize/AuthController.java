package com.example.shop.controllers.authorize;

import com.example.shop.model.DTO.LoginDto;
import com.example.shop.model.DTO.SignUpDto;
import com.example.shop.model.Role;
import com.example.shop.model.ShopUser;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.ShopUserRepository;
import com.example.shop.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ShopUserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        // генерируем токен
        String jwts = jwtService.getToken(authentication.getName());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Создаем ответ с токеном
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Shop " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                        "Authorization")
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        System.out.println("@PostMapping(/signup) ");
        // add check for username exists in a DB
        if(userRepository.existsByUserName(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // create user object
        ShopUser user = new ShopUser();
        user.setUserName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }


}
