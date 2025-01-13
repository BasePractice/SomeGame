package ru.base.game.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.base.game.server.repository.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Authority.PK> {
}
