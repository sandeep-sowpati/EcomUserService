package dev.sansow.ecomuserservice.repository;

import dev.sansow.ecomuserservice.model.Session;
import dev.sansow.ecomuserservice.model.SessionStatus;
import dev.sansow.ecomuserservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session,Long> {
    Optional<Session> findSessionByTokenAndUser_IdAndSessionStatus(String token, Long user_id, SessionStatus sessionStatus);
    List<Session> findSessionsByUser_IdAndSessionStatus(Long user_id, SessionStatus sessionStatus);
}
