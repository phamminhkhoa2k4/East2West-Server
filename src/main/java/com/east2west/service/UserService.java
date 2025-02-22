package com.east2west.service;

import com.east2west.models.DTO.SignupRequest;
import com.east2west.models.Entity.ERole;
import com.east2west.models.Entity.Role;
import com.east2west.models.payload.request.UpdateProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.east2west.models.Entity.User;
import com.east2west.repository.*;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone){
        return userRepository.existsByPhone(phone);
    }

    public void registerUser(SignupRequest signUpRequest){
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .firstname(signUpRequest.getFirstname())
                .lastname(signUpRequest.getLastname())
                .phone(signUpRequest.getPhone())
                .address(signUpRequest.getAddress())
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole  = roleRepository.findByRoleName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            switch (strRoles.iterator().next()) {
                case "business":
                    Role businessRole = roleRepository.findByRoleName(ERole.BUSINESS)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(businessRole);
                    break;
                case "mod":
                    Role modRole = roleRepository.findByRoleName(ERole.MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);
                    break;
                case "employee":
                    Role employeeRole = roleRepository.findByRoleName(ERole.EMPLOYEE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(employeeRole);
                    break;
                default:
                    Role userRole = roleRepository.findByRoleName(ERole.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
            }
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    public void updateUser(User existingUser, UpdateProfileRequest updateRequest) {
        existingUser.setUsername(updateRequest.getUsername());
        existingUser.setFirstname(updateRequest.getFirstname());
        existingUser.setLastname(updateRequest.getLastname());
        existingUser.setEmail(updateRequest.getEmail());
        existingUser.setPhone(updateRequest.getPhone());
        existingUser.setAddress(updateRequest.getAddress());
        userRepository.save(existingUser);
    }

    public  Optional<User> findByEmail(String email) {
       return userRepository.findByEmail(email);
    }

    public Role findByRoleName(ERole eRole) {
        Optional<Role> role = roleRepository.findByRoleName(eRole);
        return role.orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }



}
