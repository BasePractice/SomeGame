package ru.base.game.engine;

public interface DataManipulation<E> {

    void write(E element);

    E read();
}
