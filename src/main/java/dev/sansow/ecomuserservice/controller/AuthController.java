package dev.sansow.ecomuserservice.controller;

import dev.sansow.ecomuserservice.dto.LogOutRequestDTO;
import dev.sansow.ecomuserservice.dto.UserResponseDTO;
import dev.sansow.ecomuserservice.dto.UserSignUpRequestDTO;
import dev.sansow.ecomuserservice.exceptions.InvalidCredentialsException;
import dev.sansow.ecomuserservice.exceptions.SessionExpiredException;
import dev.sansow.ecomuserservice.exceptions.SessionNotFoundException;
import dev.sansow.ecomuserservice.exceptions.UserNotFoundException;
import dev.sansow.ecomuserservice.model.SessionStatus;
import dev.sansow.ecomuserservice.model.User;
import dev.sansow.ecomuserservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserSignUpRequestDTO userSignUpRequestDTO){
        UserResponseDTO userResponseDTO = authService.signUp(userSignUpRequestDTO.getEmail(), userSignUpRequestDTO.getPassword());
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello world");
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserSignUpRequestDTO userSignUpRequestDTO) throws UserNotFoundException, InvalidCredentialsException, SessionNotFoundException {
        return authService.login(userSignUpRequestDTO.getEmail(), userSignUpRequestDTO.getPassword());
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<Boolean> logOut(@PathVariable("id") Long id,@RequestHeader String token ) throws SessionNotFoundException {
        authService.logOut(id, token);
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validate(@RequestBody LogOutRequestDTO logOutRequestDTO) throws SessionExpiredException, SessionNotFoundException {
        SessionStatus sessionStatus = authService.validate(logOutRequestDTO.getToken(), logOutRequestDTO.getUserId());
        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }
}
