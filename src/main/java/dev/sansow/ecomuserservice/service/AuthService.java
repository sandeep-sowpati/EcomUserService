package dev.sansow.ecomuserservice.service;

import dev.sansow.ecomuserservice.dto.UserResponseDTO;
import dev.sansow.ecomuserservice.exceptions.InvalidCredentialsException;
import dev.sansow.ecomuserservice.exceptions.SessionExpiredException;
import dev.sansow.ecomuserservice.exceptions.SessionNotFoundException;
import dev.sansow.ecomuserservice.exceptions.UserNotFoundException;
import dev.sansow.ecomuserservice.model.Session;
import dev.sansow.ecomuserservice.model.SessionStatus;
import dev.sansow.ecomuserservice.model.User;
import dev.sansow.ecomuserservice.repository.SessionRepo;
import dev.sansow.ecomuserservice.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.logging.Logger;

import static dev.sansow.ecomuserservice.mapper.UserMapper.convertUserToUserResponseDTO;

@Service
public class AuthService {
    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Value("${security.jwt.secret-key}")
    private String secretKey;


    public AuthService(UserRepo userRepo, SessionRepo sessionRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserResponseDTO signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        String updatedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(updatedPassword);
        User savedUser = userRepo.save(user);
        return convertUserToUserResponseDTO(savedUser);
    }

    public ResponseEntity<UserResponseDTO> login(String email, String password) throws UserNotFoundException, InvalidCredentialsException, SessionNotFoundException {

        // Getting User with provided email
        Optional<User> userOptional = userRepo.findUserByEmail(email);
        if (userOptional.isEmpty()) throw new UserNotFoundException("User With Given email Doesn't exist");

        // Comparing the Password
        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Given Email and Password are invalid");
        }

        //Get and Restrict Sessions to be less than or equals to 2

        List<Session> activeSessions = sessionRepo.findSessionsByUser_IdAndSessionStatus(user.getId(),SessionStatus.ACTIVE);
        if(activeSessions.size()>=2){
            Session session = activeSessions.get(0);
            logOut(user.getId(),session.getToken());
        }

        // If Matching create a JWT Token
        MacAlgorithm algorithm = Jwts.SIG.HS256; // Provided Algorithms for the JWT


        // Creating a Secret for the JWT

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        claims.put("roles",user.getRoles());

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH,5);

        Map<String,String> jwtHeaders= new HashMap<>();
        jwtHeaders.put("Key","test1");

//        String jwtToken = RandomStringUtils.randomAlphanumeric(15);
        String jwtToken = Jwts.builder()
                .claims(claims)
                .issuedAt(currentDate)
                .expiration(calendar.getTime())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setHeader(jwtHeaders)
                .compact();

        // Create New Session and Store the token there
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setUser(user);
        session.setToken(jwtToken);
        session.setLoggedInAt(new Date());
        sessionRepo.save(session);

        // Create a UserResponseDTO and store the current object there
        UserResponseDTO savedUser = convertUserToUserResponseDTO(user);
        // Return the response entity of UserObject

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());

        headers.add(HttpHeaders.SET_COOKIE, "auth_token:" + jwtToken);
        return new ResponseEntity<>(savedUser, headers, HttpStatus.ACCEPTED);
    }

    public void logOut(Long userId, String token) throws SessionNotFoundException {
        Optional<Session> requestedSession = sessionRepo.findSessionByTokenAndUser_IdAndSessionStatus(token,userId, SessionStatus.ACTIVE);
        if(requestedSession.isEmpty()){
            throw new SessionNotFoundException("There is no active session exists with the given Data");
        }
        Session session = requestedSession.get();
        session.setLoggedOutAt(new Date());
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepo.save(session);
    }

    public SessionStatus validate(String token,Long userId) throws SessionExpiredException, SessionNotFoundException {
        //Use JWT Parser to check the expiry from the token
        try {
            Claims claims = extractAllClaims(token);
            if (claims.getExpiration().before(new Date())) {
                return SessionStatus.ENDED;
            }
        }
        catch (SignatureException signatureException){
            return SessionStatus.INVALID;
        }
        //Check if it exists in Database
        Optional<Session> givenSession = sessionRepo.findSessionByTokenAndUser_IdAndSessionStatus(token,userId,SessionStatus.ACTIVE);
        if(givenSession.isEmpty()) return SessionStatus.INVALID;
        //return Active
        return SessionStatus.ACTIVE;
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
