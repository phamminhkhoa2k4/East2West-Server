package com.east2west.controllers;

import java.util.*;
import java.util.stream.Collectors;
import com.east2west.models.DTO.*;
import com.east2west.models.payload.request.UpdateProfileRequest;
import com.east2west.models.payload.response.JwtResponse;
import com.east2west.security.services.UserDetailsServiceImpl;
import com.east2west.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.east2west.models.Entity.User;
import com.east2west.security.jwt.JwtUtils;
import com.east2west.security.services.UserDetailsImpl;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController (AuthenticationManager authenticationManager, UserService userService, UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    public UserDetailsImpl authentication(String username, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    @PostMapping("/signin")
    public ResponseEntity<ModelResponse<?>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            userDetailsService.loadUserByUsername(loginRequest.getUsername());
            UserDetailsImpl authenticatedUserDetails = authentication(loginRequest.getUsername(),loginRequest.getPassword());
            List<String> roles = authenticatedUserDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwt = jwtUtils.generateJwtToken(authenticatedUserDetails.getUsername(),roles.toString(), loginRequest.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(ModelResponse.builder().status(HttpStatus.OK.value()).message("Authentication successful").data(JwtResponse.builder()
                    .token(jwt)
                    .userId(authenticatedUserDetails.getUserId())
                    .username(authenticatedUserDetails.getUsername())
                    .firstname(authenticatedUserDetails.getFirstname())
                    .lastname(authenticatedUserDetails.getLastname())
                    .password(authenticatedUserDetails.getPassword())
                    .email(authenticatedUserDetails.getEmail())
                    .phone(authenticatedUserDetails.getPhone()).address(authenticatedUserDetails.getAddress())
                    .roles(roles)
                    .build())
                    .build());





        } catch (UsernameNotFoundException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("username", "Account does not exist  !!!");
            ModelResponse<Object> response = ModelResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Account invalid !!!")
                    .data(errors)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BadCredentialsException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("password", "Wrong password !!!");
            ModelResponse<Object> response = ModelResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Password invalid !!!")
                    .data(errors)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("general", "System error !!!");
            ModelResponse<Object> response = ModelResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Please enter later !!!")
                    .data(errors)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        Map<String, String> errors = new HashMap<>();

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            errors.put("username", "Username is already taken!");
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            errors.put("email", "Email is already in use!");
        }
        if (userService.existsByPhone(signUpRequest.getPhone())) {
            errors.put("phone", "Phone number is already in use!");
        }
        if(!errors.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(400)
                            .message("Data is already in use!")
                            .data(errors)
                            .build());
        }


        userService.registerUser(signUpRequest);
        try {
            UserDetailsImpl userDetails =  authentication( signUpRequest.getUsername(), signUpRequest.getPassword());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(),roles.toString(), signUpRequest.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.builder()
                            .status(201)
                            .message("OK")
                            .data(
                                    JwtResponse.builder()
                                                    .token(jwt)
                                                    .userId(userDetails.getUserId())
                                                    .username(userDetails.getUsername())
                                                    .firstname(userDetails.getFirstname())
                                                    .lastname(userDetails.getLastname())
                                                    .password(userDetails.getPassword())
                                                    .email(userDetails.getEmail())
                                                    .phone(userDetails.getPhone())
                                                    .address(userDetails.getAddress())
                                                    .roles(roles)
                                                    .build())
                                                    .build());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during auto login after registration"));
        }
    }

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



    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @Valid @RequestBody UpdateProfileRequest updateRequest,
                                        @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : null;
        if (token == null || !jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Unauthorized"));
        }

        String usernameFromToken = jwtUtils.getUserNameFromJwtToken(token);
        String passwordFromToken = jwtUtils.getPasswordFromJwtToken(token);

        Optional<User> userData = userService.getUserById(id);
        if (userData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found!"));
        }
        User existingUser = userData.get();
        Map<String, String> errors = new HashMap<>();

        if (!existingUser.getUsername().equals(usernameFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("You can only update your own profile"));
        }

        if (userService.existsByUsername(updateRequest.getUsername()) &&
                !existingUser.getUsername().equals(updateRequest.getUsername())) {
            errors.put("username", "Username is already taken!");
        }
        if (userService.existsByEmail(updateRequest.getEmail()) &&
                !existingUser.getEmail().equals(updateRequest.getEmail())) {
            errors.put("email", "Email is already in use!");
        }
        if (userService.existsByPhone(updateRequest.getPhone()) &&
                !existingUser.getPhone().equals(updateRequest.getPhone())) {
            errors.put("phone", "Phone number is already in use!");
        }

        if(!errors.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(400)
                            .message("Data is already in use!")
                            .data(errors)
                            .build());
        }

        userService.updateUser(existingUser, updateRequest);

        try {
            UserDetailsImpl userDetails = authentication( updateRequest.getUsername(), passwordFromToken);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(),roles.toString(),passwordFromToken);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(200)
                            .message("OK")
                            .data(
                                    JwtResponse.builder()
                                            .token(jwt)
                                            .userId(userDetails.getUserId())
                                            .username(userDetails.getUsername())
                                            .firstname(userDetails.getFirstname())
                                            .lastname(userDetails.getLastname())
                                            .password(userDetails.getPassword())
                                            .email(userDetails.getEmail())
                                            .phone(userDetails.getPhone())
                                            .address(userDetails.getAddress())
                                            .roles(roles)
                                            .build())
                            .build());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e);
        }
    }


    @GetMapping("/verify-authenticate")
    public ResponseEntity<?> verifyToken(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ModelResponse.builder()
                            .status(404)
                            .message("Token is missing")
                            .data(null)
                            .build()
            );
        }
        String token = authHeader.substring(7);



        boolean isValid = jwtUtils.validateJwtToken(token);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ModelResponse.builder()
                            .status(403)
                            .message("Invalid or expired token")
                            .data(null)
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ModelResponse.builder()
                        .status(200)
                        .message("Token is valid")
                        .data(null)
                        .build()
        );
    }


    @GetMapping("/verify-authorize")
    public ResponseEntity<?> verifyRoles(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(403)
                            .message("No token provided")
                            .data(null)
                            .build()
            );
        }

        String token = authHeader.substring(7);

        if (!jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(403)
                            .message("Invalid or expired token")
                            .data(null)
                            .build()
            );
        }

        try {
            String username = jwtUtils.getUserNameFromJwtToken(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            List<String> userRoles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());


            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userRoles", userRoles);

            return ResponseEntity.ok(
                    ModelResponse.builder()
                            .status(200)
                            .message("OK")
                            .data(responseData)
                            .build()
            );

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ModelResponse.builder()
                            .status(404)
                            .message("User not found")
                            .data(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.builder()
                            .status(500)
                            .message("Error verifying roles")
                            .data(null)
                            .build()
            );
        }
    }

}
