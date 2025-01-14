package ru.base.game.server.dto;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.base.game.engine.Map;
import ru.base.game.engine.Player;
import ru.base.game.engine.map.StandardMapGenerator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("MapObject")
class MapObjectTest {
    @DisplayName("serialize")
    @Test
    void testSerialize() {
        Map generated = new StandardMapGenerator().generate(1, 10, 10, Map.Generator.Kind.D2D);
        Map.Coordinated<Map.Matrix> matrix = generated.matrix(5, 5, 2);
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(new RefreshObject(new Player(), matrix.source()));
        assertNotNull(json);
        System.out.println(json);
    }
}
