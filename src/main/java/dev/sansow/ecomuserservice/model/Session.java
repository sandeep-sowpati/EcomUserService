package dev.sansow.ecomuserservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Session extends BaseModel{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String token;
    private Date loggedInAt;
    private Date loggedOutAt;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;

}
