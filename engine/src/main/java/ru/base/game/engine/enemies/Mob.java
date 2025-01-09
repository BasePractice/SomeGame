package ru.base.game.engine.enemies;

import ru.base.game.engine.Context;
import ru.base.game.engine.Enemy;
import ru.base.game.engine.items.DamagedItem;

public abstract class Mob implements Enemy {
    protected final DamagedItem selectedItem;
    protected int health;
    protected boolean isAggressive;
    protected boolean inBattle;

    public Mob(DamagedItem selectedItem, int health, boolean isAggressive) {
        this.selectedItem = selectedItem;
        this.health = health;
        this.isAggressive = isAggressive;
    }

    @Override
    public boolean battleOn() {
        if (isAggressive) {
            inBattle = true;
            return true;
        }
        return false;
    }

    @Override
    public void attack(DamagedItem selectedItem) {
        inBattle = true;
        health -= selectedItem.damage();
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public void tick(Context context) {
        if (inBattle) {
            context.attack(selectedItem);
        }
    }
}
