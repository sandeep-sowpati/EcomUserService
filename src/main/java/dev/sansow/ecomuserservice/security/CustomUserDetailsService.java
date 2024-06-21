package dev.sansow.ecomuserservice.security;

import dev.sansow.ecomuserservice.model.User;
import dev.sansow.ecomuserservice.repository.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findUserByEmail(username);

        if(user.isEmpty()) throw new UsernameNotFoundException("Given user doesnt exists in our System");

        User user1 = user.get();

        return new CustomUserDetails(user1);

    }
}
