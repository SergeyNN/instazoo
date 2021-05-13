package com.example.instazoo.services;

import com.example.instazoo.dto.UserDTO;
import com.example.instazoo.entity.UserEntity;
import com.example.instazoo.entity.enums.ERole;
import com.example.instazoo.exceptions.UserExistException;
import com.example.instazoo.payload.request.SignupRequest;
import com.example.instazoo.repository.UserRepository;
import org.omg.CORBA.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(SignupRequest userIn) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userIn.getEmail());
        userEntity.setUserName(userIn.getUsername());
        userEntity.setLastName(userIn.getLastname());
        userEntity.setFirstName(userIn.getFirstname());
        userEntity.setPassword(passwordEncoder.encode(userIn.getPassword()));
        userEntity.getRole().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(userEntity);
        } catch (Exception e) {
            LOG.error("Error during registration {}", e.getMessage());
            throw new UserExistException("The user " + userEntity.getUsername() + " already exist. Please check credentials");
        }
    }

    public UserEntity updateUser(UserDTO userDTO, Principal principal) {
        UserEntity user = getUserByPrincipal(principal);
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userRepository.save(user);
    }

    public UserEntity getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserEntityByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
