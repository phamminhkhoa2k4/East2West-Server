package com.east2west.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.east2west.service.UserService;
import jakarta.validation.Valid;

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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.east2west.models.DTO.LoginRequest;
import com.east2west.models.DTO.MessageResponse;
import com.east2west.models.DTO.SignupRequest;
import com.east2west.models.DTO.UpdateUserRequest;
import com.east2west.models.DTO.UserInfoResponse;
import com.east2west.models.Entity.ERole;
import com.east2west.models.Entity.Role;
import com.east2west.models.Entity.User;
import com.east2west.repository.RoleRepository;
import com.east2west.repository.UserRepository;
import com.east2west.security.jwt.JwtUtils;
import com.east2west.security.services.UserDetailsImpl;

//for Angular Client (withCredentials)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

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
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
    
            SecurityContextHolder.getContext().setAuthentication(authentication);
    
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
    
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
    
            // Create response body
            UserInfoResponse userInfoResponse = new UserInfoResponse(
                    userDetails.getUserId(),
                    userDetails.getUsername(),
                    userDetails.getFirstname(),
                    userDetails.getLastname(),
                    userDetails.getEmail(),
                    userDetails.getPhone(),
                    userDetails.getAddress(),
                    roles);
    
            // Return response with Set-Cookie header
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(userInfoResponse);
    
        } catch (Exception e) {
            // Return bad credentials response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Bad credentials"));
        }
    }
    

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("username", "Username is already taken!");
            return ResponseEntity.badRequest().body(errors);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("email", "Email is already in use!");
            return ResponseEntity.badRequest().body(errors);
        }
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("phone", "Phone number is already in use!");
            return ResponseEntity.badRequest().body(errors);
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
            role = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            switch (strRoles.iterator().next()) {
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

    // Exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{id}")
public ResponseEntity<?> updateUser(@PathVariable int id, @Valid @RequestBody UpdateUserRequest updateRequest) {
    Optional<User> userData = userRepository.findById(id);

    if (!userData.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found!"));
    }

    User existingUser = userData.get();

    // Kiểm tra trùng lặp
    if (userRepository.existsByUsername(updateRequest.getUsername()) &&
            !existingUser.getUsername().equals(updateRequest.getUsername())) {
                Map<String, String> errors = new HashMap<>();
                errors.put("username", "Username is already taken!");
                return ResponseEntity.badRequest().body(errors);
    }

    if (userRepository.existsByEmail(updateRequest.getEmail()) &&
            !existingUser.getEmail().equals(updateRequest.getEmail())) {
                Map<String, String> errors = new HashMap<>();
                errors.put("email", "Email is already in use!");
                return ResponseEntity.badRequest().body(errors);
    }

    if (userRepository.existsByPhone(updateRequest.getPhone()) &&
            !existingUser.getPhone().equals(updateRequest.getPhone())) {
                Map<String, String> errors = new HashMap<>();
                errors.put("phone", "Phone number is already in use!");
                return ResponseEntity.badRequest().body(errors);
    }

    // Cập nhật thông tin người dùng
    existingUser.setUsername(updateRequest.getUsername());
    existingUser.setFirstname(updateRequest.getFirstname());
    existingUser.setLastname(updateRequest.getLastname());
    existingUser.setEmail(updateRequest.getEmail());
    existingUser.setPhone(updateRequest.getPhone());
    existingUser.setAddress(updateRequest.getAddress());
    // Không thay đổi mật khẩu
    userRepository.save(existingUser);

    return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
}

}
