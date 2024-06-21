package dev.sansow.ecomuserservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOutRequestDTO {
    private Long userId;
    private String token;
}
