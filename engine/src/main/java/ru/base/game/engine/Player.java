package ru.base.game.engine;

import ru.base.game.engine.items.DamagedItem;
import ru.base.game.engine.items.Fist;

public final class Player implements Element {
    final DamagedItem[] inventory = new DamagedItem[10];
    DamagedItem selectedItem = new Fist();
    int health = 100;
    int x;
    int y;

    @Override
    public void tick(Context context) {
        //Nothing
    }

    public void flyTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean took(DamagedItem item) {
        int slot = getFreeSlot();
        if (slot >= 0 && slot < inventory.length) {
            inventory[slot] = item;
            return true;
        }
        return false;
    }

    private int getFreeSlot() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void hit(int damage) {
        health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }
}
