package ru.base.game.engine.items;

import ru.base.game.engine.Context;
import ru.base.game.engine.Item;

public class DamagedItem implements Item {
    protected final int damage;

    public DamagedItem(int damage) {
        this.damage = damage;
    }

    @Override
    public void tick(Context context) {
        //Nothing
    }

    @Override
    public int damage() {
        return damage;
    }
}
