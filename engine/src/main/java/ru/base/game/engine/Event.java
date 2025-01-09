package ru.base.game.engine;

public interface Event extends Element {

    Type type();

    Object source();

    boolean activate(Context context);

    enum Type {
        ENTER, EXIT
    }
}
