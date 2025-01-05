package ru.base.game.engine;

public interface Map {
    enum BlockType {
        EMPTY, WALL
    }

    enum ElementType {
        ENEMY, ITEM, EVENT, ENTER, EXIT
    }
}
