package dev.sansow.ecomuserservice.service;

import dev.sansow.ecomuserservice.dto.UserResponseDTO;
import dev.sansow.ecomuserservice.exceptions.InvalidRolesProvidedException;
import dev.sansow.ecomuserservice.exceptions.SessionNotFoundException;
import dev.sansow.ecomuserservice.exceptions.UserNotFoundException;
import dev.sansow.ecomuserservice.model.Role;
import dev.sansow.ecomuserservice.model.Session;
import dev.sansow.ecomuserservice.model.SessionStatus;
import dev.sansow.ecomuserservice.model.User;
import dev.sansow.ecomuserservice.repository.RoleRepo;
import dev.sansow.ecomuserservice.repository.SessionRepo;
import dev.sansow.ecomuserservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.sansow.ecomuserservice.mapper.UserMapper.convertUserToUserResponseDTO;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final SessionRepo sessionRepo;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, SessionRepo sessionRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.sessionRepo = sessionRepo;
    }

    public UserResponseDTO getUserDetails(Long id) throws UserNotFoundException {
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty()) throw new UserNotFoundException("User with Id" + id + "Not Found" );
        User user = userOptional.get();
        return convertUserToUserResponseDTO(user);

    }

    public UserResponseDTO assignUserRoles(Long userId, List<Long> roleIds) throws UserNotFoundException, InvalidRolesProvidedException {
        Set<Role> roles = roleRepo.findAllByIdIn(roleIds);
        Optional<User> userOptional = userRepo.findById(userId);
        if(userOptional.isEmpty()) throw new UserNotFoundException("User with Given id is not found");
        if (roles.isEmpty()) throw new InvalidRolesProvidedException("Given Role Ids not Found please give new Roles");
        User user = userOptional.get();
        roles.addAll(user.getRoles());
        user.setRoles(roles);

        return convertUserToUserResponseDTO(userRepo.save(user));
    }

    public List<User> allUsers(){
        return userRepo.findAll();
    }

    public List<Session> allSessions(){
        return sessionRepo.findAll();
    }


}
