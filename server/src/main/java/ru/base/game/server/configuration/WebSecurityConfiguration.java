package ru.base.game.server.configuration;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.base.game.server.repository.AuthorityRepository;
import ru.base.game.server.repository.UserRepository;
import ru.base.game.server.repository.entity.Authority;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> {
            requests
                .requestMatchers(
                    "/web/css/**",
                    "/web/js/**",
                    "/web/json/**",
                    "/web/images/**",
                    "/login",
                    "/error",
                    "/actuator"
                ).permitAll()
                .anyRequest().authenticated();
        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(login -> {
            login.defaultSuccessUrl("/web/index.html");
        });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    @EventListener(ApplicationReadyEvent.class)
    private void afterConfigure(ApplicationReadyEvent event) {
        UserRepository repository = event.getApplicationContext().getBean(UserRepository.class);
        var users = repository.findAll();
        users.forEach(System.out::println);
        AuthorityRepository authorityRepository = event.getApplicationContext().getBean(AuthorityRepository.class);
        List<Authority> authorities = authorityRepository.findAll();
        authorities.forEach(System.out::println);
    }
}
