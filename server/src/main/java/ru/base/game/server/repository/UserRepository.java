package ru.base.game.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.base.game.server.repository.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
