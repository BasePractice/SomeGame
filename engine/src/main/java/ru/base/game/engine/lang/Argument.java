package ru.base.game.engine.lang;

public interface Argument {

    record Numeric(Number number) implements Argument {
        @Override
        public String toString() {
            return String.valueOf(number);
        }
    }

    final class None implements Argument {

    }
}
