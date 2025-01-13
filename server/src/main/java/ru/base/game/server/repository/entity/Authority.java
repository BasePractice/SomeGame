package ru.base.game.server.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "authorities")
@IdClass(Authority.PK.class)
public class Authority {
    @Id
    @Column(name = "username")
    private String username;
    @Id
    private String authority;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @EqualsAndHashCode
    @Embeddable
    public static final class PK implements Serializable {
        private String username;
        private String authority;
    }
}
