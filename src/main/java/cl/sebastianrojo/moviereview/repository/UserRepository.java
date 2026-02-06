package cl.sebastianrojo.moviereview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sebastianrojo.moviereview.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}