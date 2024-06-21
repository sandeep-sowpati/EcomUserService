package dev.sansow.ecomuserservice.repository;

import dev.sansow.ecomuserservice.model.Role;
import dev.sansow.ecomuserservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
    Set<Role> findAllByIdIn(List<Long> roleIds);
}
