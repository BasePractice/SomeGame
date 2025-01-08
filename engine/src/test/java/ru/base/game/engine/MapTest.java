package ru.base.game.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Map")
class MapTest {
    @DisplayName("toString")
    @Test
    void testToString() {
        Map generated = Map.generator().generate(1, 21, 3, Map.Generator.Kind.D1D);
        assertNotNull(generated);
        System.out.println(generated);
    }
}
