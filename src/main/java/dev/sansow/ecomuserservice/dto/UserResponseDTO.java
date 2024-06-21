package dev.sansow.ecomuserservice.dto;

import dev.sansow.ecomuserservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String email;
    private Set<Role> roles;
}
