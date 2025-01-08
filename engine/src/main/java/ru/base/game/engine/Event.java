package ru.base.game.engine;

public interface Event extends Element {

    Type type();

    Object source();

    enum Type {
        ENTER, EXIT
    }

    static Event of(Type type, Object source) {
        return new Default(type, source);
    }

    record Default(Type type, Object source) implements Event {
        @Override
        public void tick() {
            //Nothing
        }
    }
}
