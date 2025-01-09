package ru.base.game.engine;

import ru.base.game.engine.items.DamagedItem;

public interface Enemy extends Element {

    boolean battleOn();

    void attack(DamagedItem selectedItem);

    boolean isDead();
}
