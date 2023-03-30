package com.example.shop.controllers.authorize;

import com.example.shop.model.DTO.LoginDto;
import com.example.shop.model.DTO.SignUpDto;
import com.example.shop.model.DTO.VerifyTokenDto;
import com.example.shop.model.Role;
import com.example.shop.model.ShopUser;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.ShopUserRepository;
import com.example.shop.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@CrossOrigin(origins = "http://localhost:3000" )
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
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        // генерируем токен
        String jwts = jwtService.getToken(authentication.getName());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Создаем ответ с токеном
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,  jwts);
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,"Authorization");
        System.out.println("Авторизация....."+jwts);
        return ResponseEntity.ok()
                .headers(headers)
                .body(loginDto.getUsernameOrEmail());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

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


    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        //ResponseCookie cookie = jwtService.getCleanJwtCookie();
        session.invalidate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,  "");
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,"Authorization");
        return ResponseEntity.ok().headers(headers)
                .body("Logout successful!!!");
    }




}
