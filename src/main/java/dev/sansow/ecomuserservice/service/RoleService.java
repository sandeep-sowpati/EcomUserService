package dev.sansow.ecomuserservice.service;

import dev.sansow.ecomuserservice.model.Role;
import dev.sansow.ecomuserservice.repository.RoleRepo;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role createRole(String name){
        Role role = new Role();
        role.setRoleName(name);
        return roleRepo.save(role);
    }
}
