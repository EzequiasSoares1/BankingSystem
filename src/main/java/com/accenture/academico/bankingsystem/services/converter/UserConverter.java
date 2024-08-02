package com.accenture.academico.bankingsystem.services.converter;

import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserConverter {

    public static User convertToUser(UserDTO registrerDTO) {
        return new User(
                registrerDTO.id(),
                registrerDTO.email(),
                converterRole(registrerDTO.role()),
                registrerDTO.password(),
                null
        );
    }

    public static UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                null,
                user.getRole().toString()
        );
    }
    public static List<UserDTO> convertToUserDTOList(List<User> users) {
        return users.stream()
                .map(UserConverter::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private static Role converterRole(String role){
      return Role.valueOf(role.toUpperCase());
    }
}
