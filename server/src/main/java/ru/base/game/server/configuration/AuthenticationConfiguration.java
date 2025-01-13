package ru.base.game.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class AuthenticationConfiguration {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource, PasswordEncoder encoder) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
            .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
            .authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username=?")
            .withUser(User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN"));
    }
}
