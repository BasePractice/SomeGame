package ru.base.game.engine.enemies;

import ru.base.game.engine.Enemy;
import ru.base.game.engine.Item;

public abstract class Mob implements Enemy {
    protected final Item selectedItem;
    protected boolean inBattle;

    public Mob(Item selectedItem) {
        this.selectedItem = selectedItem;
    }
}
