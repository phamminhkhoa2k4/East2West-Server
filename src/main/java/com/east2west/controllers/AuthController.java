package com.east2west.controllers;

import java.util.*;
import java.util.stream.Collectors;
import com.east2west.models.DTO.*;
import com.east2west.models.Entity.ERole;
import com.east2west.models.Entity.PasswordResetToken;
import com.east2west.models.Entity.Role;
import com.east2west.models.payload.request.*;
import com.east2west.models.payload.response.ErrorResponse;
import com.east2west.models.payload.response.JwtResponse;
import com.east2west.security.services.UserDetailsServiceImpl;
import com.east2west.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.east2west.models.Entity.User;
import com.east2west.security.jwt.JwtUtils;
import com.east2west.security.services.UserDetailsImpl;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final UserDetailsServiceImpl userDetailsService;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${app.oauth2.redirectUri}")
    private String GOOGLE_REDIRECT_URI;
    private final RestTemplate restTemplate = new RestTemplate();





    @Autowired
    public AuthController (AuthenticationManager authenticationManager, UserService userService, UserDetailsServiceImpl userDetailsService, PasswordEncoder encoder, JwtUtils jwtUtils){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public UserDetailsImpl authentication(String username, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (UserDetailsImpl) authentication.getPrincipal();
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            userService.sendVerificationCode(request.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(200)
                            .message("Sent authenticate code !!!")
                            .data("OK")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.builder()
                            .status(400)
                            .message("Can't send authenticate code !!!")
                            .data("null")
                            .build()
            );
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequest request) {
        boolean isValid = userService.verifyCode(request.getEmail(), request.getVerificationCode());
        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(200)
                            .message("Authenticate code valid !!!")
                            .data("OK")
                            .build()
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ModelResponse.builder()
                        .status(400)
                        .message("Authenticate code invalid !!!")
                        .data("OK")
                        .build()
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            Optional<User> user = userService.findByEmail(request.getEmail());
            if(user.isPresent()){
                userService.resetPassword(request.getEmail(), request.getVerificationCode(), request.getNewPassword(),user.get());
                UserDetailsImpl  authenticatedUserDetails = authentication(user.get().getUsername(),request.getNewPassword());
                List<String> roles = authenticatedUserDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();
                String jwt = jwtUtils.generateJwtToken(authenticatedUserDetails.getUsername(),roles, request.getNewPassword());

                return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(ModelResponse.builder().status(HttpStatus.OK.value()).message("Authentication successful").data(JwtResponse.builder()
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
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ModelResponse.builder()
                            .status(404)
                            .message("User not found !!!")
                            .data(null)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.builder()
                            .status(400)
                            .message("Can not reset password !!!")
                            .data("OK")
                            .build()
            );
        }
    }



    // change password

    @PostMapping("/change-password-request")
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<User> user = userService.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("User not exist !!!")
                            .data(null)
                            .build()
            );
        }
        try{
            Optional<PasswordResetToken> Token = userService.findTokenByUserId(user.get().getUserId());
            if (Token.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.builder()
                                .status(400)
                                .message("Please check this request email was sent earlier !!!")
                                .data(null)
                                .build()
                );
            }
            userService.createPasswordResetTokenForUser(user.get());


        }catch (Exception ex){
            System.out.println(ex);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                ModelResponse.builder()
                        .status(200)
                        .message("Ok")
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> savePassword(@RequestBody ChangePasswordRequest request) {
        String result = userService.validatePasswordResetToken(request.getToken());
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(400)
                            .message("Invalid Token")
                            .data(null)
                            .build()
            );
        }

        User user = userService.getUserByPasswordResetToken(request.getToken());
        userService.changeUserPassword(user, request.getNewPassword());

        UserDetailsImpl authenticatedUserDetails = authentication(user.getUsername(),request.getNewPassword());
        List<String> roles = authenticatedUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(authenticatedUserDetails.getUsername(),roles.toString(), request.getNewPassword());

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(ModelResponse.builder().status(HttpStatus.OK.value()).message("Authentication successful").data(JwtResponse.builder()
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
    }


    // sms
    // TODO : complete sms change-password
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody ChangePasswordSMSRequest request) {
        String otp = generateOtp();
        try {
            userService.storeOtp(request.getPhoneNumber(), otp);
            userService.sendOtp(request.getPhoneNumber(), otp);

        }catch (Exception ex){
            System.out.println(ex);
        }

        return "OTP sent successfully";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp, @RequestParam String newPassword) {
        String storedOtp = userService.getOtp(phoneNumber);

        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP hợp lệ, đổi mật khẩu
            User user = userService.findByPhoneNumber(phoneNumber);
            if (user != null) {
                user.setPassword(encoder.encode(newPassword));
                userService.save(user);
                userService.clearOtp(phoneNumber);
                return "Password changed successfully";
            } else {
                return "User not found";
            }
        } else {
            return "Invalid OTP";
        }
    }

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }


    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri",GOOGLE_REDIRECT_URI);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // Gửi yêu cầu và bắt lỗi
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            System.out.println("Access Token Response: " + response.getBody());
                    if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Google OAuth Error: " + response.getBody());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + response.getBody());
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = response.getBody();
            String accessToken = (String) responseBody.get("access_token");
            String idToken = (String) responseBody.get("id_token");

            // Lấy thông tin người dùng từ Google API
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);


            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.GET,
                    userInfoRequest,
                    Map.class
            );


            if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> userInfo = userInfoResponse.getBody();

                String email = (String) userInfo.get("email");
                String givenName = (String) userInfo.get("given_name");
                String familyName = (String) userInfo.get("family_name");
                String googleId = (String) userInfo.get("id");

                String username = givenName.toLowerCase().replaceAll("\\s+", "") + "." + familyName.toLowerCase().replaceAll("\\s+", "");

                if (userService.existsByUsername(username)) {
                    username = username + "." + googleId;
                }


                String finalUsername = username;
                Set<Role> roles = new HashSet<>();
                Role userRole = userService.findByRoleName(ERole.USER);
                roles.add(userRole);
                    User user = userService.findByEmail(email)
                            .orElseGet(() -> {
                                User.UserBuilder builder = User.builder();
                                builder.email(email);
                                builder.username(finalUsername);
                                builder.phone(null);
                                builder.address(null);
                                builder.firstname(givenName);
                                builder.lastname(familyName);
                                builder.password(null);
                                builder.roles(roles);
                                User newUser = builder
                                        .build();

                                return userService.save(newUser);
                            });
                List<String> roleNames = roles.stream()
                        .map(role -> role.getRoleName().name())
                        .collect(Collectors.toList());

           
                String jwt = jwtUtils.generateJwtToken(user.getUsername(),roleNames, idToken);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                        ModelResponse.builder()
                                .status(201)
                                .message("OK")
                                .data(
                                        JwtResponse.builder()
                                                .token(jwt)
                                                .userId(user.getUserId())
                                                .username(user.getUsername())
                                                .firstname(user.getFirstname())
                                                .lastname(user.getLastname())
                                                .password(user.getPassword())
                                                .email(user.getEmail())
                                                .phone(user.getPhone())
                                                .address(user.getAddress())
                                                .roles(roleNames)
                                                .build())
                                .build());

            }

        }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Status Code: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }



        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }

    private String createJwtToken(String idToken) {
        // TODO: Tạo JWT token dựa trên idToken từ Google
        return "your-jwt-token";
    }

    @PostMapping("/signin")
    public ResponseEntity<ModelResponse<?>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            userDetailsService.loadUserByUsername(loginRequest.getUsername());
            UserDetailsImpl authenticatedUserDetails = authentication(loginRequest.getUsername(),loginRequest.getPassword());
            List<String> roles = authenticatedUserDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwt = jwtUtils.generateJwtToken(authenticatedUserDetails.getUsername(),roles, loginRequest.getPassword());

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
            UserDetailsImpl userDetails =  authentication(signUpRequest.getUsername(), signUpRequest.getPassword());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(),roles, signUpRequest.getPassword());

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
            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(),roles,passwordFromToken);

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
