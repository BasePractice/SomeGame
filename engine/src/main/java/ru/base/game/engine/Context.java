package ru.base.game.engine;

import ru.base.game.engine.items.DamagedItem;

public interface Context {
    void attack(DamagedItem selectedItem);

    void nextLevel();
}
