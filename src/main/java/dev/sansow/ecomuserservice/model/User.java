package dev.sansow.ecomuserservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "ECOM_USER")
@Getter
@Setter
public class User extends BaseModel{
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    @ManyToMany
    private Set<Role> roles = new HashSet<>();
}
