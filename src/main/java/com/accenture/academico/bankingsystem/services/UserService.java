package com.accenture.academico.bankingsystem.services;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.user.UserDTO;
import com.accenture.academico.bankingsystem.exceptions.ConflictException;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.mappers.user.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(email);
        return user != null ? user: null;
    }

    public UserDTO getUserByEmail(String email){
        User u = userRepository.findUserByEmail(email);
        if(u == null){
            throw new NotFoundException("User not found");
        }
        return UserConverter.convertToUserDTO(u);
    }
    public UserDTO getUserById(UUID id){
        User u =  userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return  UserConverter.convertToUserDTO(u);
    }

    public List<UserDTO> getAllUsers() {
        List<User> u = userRepository.findAll();
        return u != null ? UserConverter.convertToUserDTOList(u): null;
    }

    public UserDTO saveUser(UserDTO userDTO) {

       if(userRepository.findByEmail(userDTO.email()) != null){
           throw new ConflictException("User already exists");
       }
        User newUser = UserConverter.convertToUser(userDTO);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return  UserConverter.convertToUserDTO(userRepository.save(newUser));
    }

    public void deleteUser(UUID id){
        getUserById(id);
        userRepository.deleteById(id);
    }

    public UserDTO update(UUID id, UserDTO userDTO){
        User user =  userRepository.findById(id).orElseThrow(() ->new NotFoundException("User not found"));

        if (userDTO.email() != null && !user.getEmail().equals(userDTO.email())) {
            if(userRepository.findByEmail(userDTO.email()) == null){
                user.setEmail(userDTO.email());
            }else {
                throw new ConflictException("Email already found");
            }
        }
        if (userDTO.password() != null) {user.setPassword(passwordEncoder.encode(userDTO.password()));}
        if (userDTO.role() != null) {user.setRole(Role.valueOf(userDTO.role()));}

       return UserConverter.convertToUserDTO(userRepository.save(user));
    }
}
