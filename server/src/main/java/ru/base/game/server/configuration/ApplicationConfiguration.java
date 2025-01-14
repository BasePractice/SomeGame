package ru.base.game.server.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.base.game.engine.Map;
import ru.base.game.engine.map.StandardMap;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
            .registerTypeAdapter(Map.class, new StandardMap.Adapter())
            .create();
    }
}
