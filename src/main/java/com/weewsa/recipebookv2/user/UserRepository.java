package com.weewsa.recipebookv2.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    @Query("SELECT u.id FROM User u WHERE u.login = :login")
    Long findIdByLogin(@Param("login") String login);
    boolean existsByLogin(String login);
}
