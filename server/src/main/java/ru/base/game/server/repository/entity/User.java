package ru.base.game.server.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.ToString;

@ToString(includeFieldNames = false, doNotUseGetters = true)
@Getter
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private String username;
    private String password;
    private String email;
    private String phone;
    private String token;
    private boolean enabled;
}
