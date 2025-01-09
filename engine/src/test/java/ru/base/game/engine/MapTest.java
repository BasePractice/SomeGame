package ru.base.game.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Map")
class MapTest {

    private static Stream<Arguments> map() {
        return Stream.of(
            Arguments.of(Map.Generator.Kind.D1D),
            Arguments.of(Map.Generator.Kind.D2D)
        );
    }

    @DisplayName("toString")
    @MethodSource("map")
    @ParameterizedTest
    void testToString(Map.Generator.Kind kind) {
        Map generated = Map.generator().generate(1, 21, 21, kind);
        assertNotNull(generated);
        System.out.println(generated);
    }
}
