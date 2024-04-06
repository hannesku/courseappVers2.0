package at.codersbay.courseapp.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsernameIgnoreCase(String username);
    public Optional<User> findByEmail(String email);


}
