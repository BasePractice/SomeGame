package ru.base.game.engine;

public interface Listener {

    <E> void emit(Event<E> event);

    interface Event<E> {
        E data();
    }

    record Refresh(Map data, Player player) implements Event<Map> {

    }
}
