package ru.base.game.engine.events;

public final class Enter extends StandardEvent {
    public Enter(Object source) {
        super(Type.ENTER, source);
    }
}
