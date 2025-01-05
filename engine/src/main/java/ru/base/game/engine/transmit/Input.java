package ru.base.game.engine.transmit;

import java.util.Optional;

public interface Input {

    String readText();

    default Optional<Integer> readInt() {
        try {
            return Optional.of(Integer.parseInt(readText()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
