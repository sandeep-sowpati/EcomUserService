package dev.sansow.ecomuserservice.mapper;

import dev.sansow.ecomuserservice.dto.UserResponseDTO;
import dev.sansow.ecomuserservice.model.User;



public class UserMapper {
    public static UserResponseDTO convertUserToUserResponseDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRoles(user.getRoles());

        return userResponseDTO;
    }
}
