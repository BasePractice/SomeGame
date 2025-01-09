package ru.base.game.engine;

import ru.base.game.engine.items.DamagedItem;

import java.util.Random;

public interface Enemy extends Element {

    boolean battleOn();

    void attack(DamagedItem selectedItem);

    Bonus dieBonus();

    boolean isDead();

    enum Bonus {
        EMPTY, ITEM, HEALTH;

        public void apply(Player player) {
            switch (this) {
                case EMPTY -> {
                    //Nothing
                }
                case ITEM -> {
                    //Nothing
                }
                case HEALTH -> {
                    player.health += new Random().nextInt(25) + 10;
                }
                default -> throw new IllegalStateException("Unexpected value: " + this);
            }
        }
    }
}
