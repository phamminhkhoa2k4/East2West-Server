package com.east2west.controllers;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.east2west.models.DTO.LoginRequest;
import com.east2west.models.DTO.MessageResponse;
import com.east2west.models.DTO.SignupRequest;
import com.east2west.models.DTO.UserInfoResponse;
import com.east2west.models.Entity.ERole;
import com.east2west.models.Entity.Role;
import com.east2west.models.Entity.User;
import com.east2west.repository.RoleRepository;
import com.east2west.repository.UserRepository;
import com.east2west.security.jwt.JwtUtils;
import com.east2west.security.services.UserDetailsImpl;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(
                        userDetails.getUserId(),
                        userDetails.getUsername(),
                        userDetails.getFirstname(),
                        userDetails.getLastname(),
                        userDetails.getEmail(),
                        userDetails.getPhone(),
                        userDetails.getAddress(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getPhone(),
                signUpRequest.getAddress());

        Set<String> strRoles = signUpRequest.getRole();
        Role role;

        if (strRoles == null || strRoles.isEmpty()) {
            // Nếu không có vai trò, gán vai trò người dùng mặc định
            role = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            // Gán vai trò dựa trên yêu cầu
            switch (strRoles.iterator().next()) { // Lấy phần tử đầu tiên của Set
                case "business":
                    role = roleRepository.findByRoleName(ERole.ROLE_BUSINESS)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    break;
                case "mod":
                    role = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    break;
                case "employee":
                    role = roleRepository.findByRoleName(ERole.ROLE_EMPLOYEE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    break;
                default:
                    role = roleRepository.findByRoleName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }
        }

        user.setRoles(role);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
