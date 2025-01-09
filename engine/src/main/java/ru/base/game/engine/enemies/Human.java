package ru.base.game.engine.enemies;

import ru.base.game.engine.items.Fist;

public final class Human extends Mob {
    public Human(int health) {
        super(new Fist(), health, false);
    }
}
