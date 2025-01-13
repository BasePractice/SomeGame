package ru.base.game.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.base.game.server.repository.UserRepository;
import ru.base.game.server.repository.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    @Slf4j
    @Service("UserService.Default")
    final class Default implements UserService {
        private final UserRepository userRepository;

        @Autowired
        public Default(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<User> find = userRepository.findUserByUsername(username);
            return find.map(Details::new).orElseThrow(() -> new UsernameNotFoundException(username));
        }

        private record Details(User user) implements UserDetails {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }
        }
    }
}
