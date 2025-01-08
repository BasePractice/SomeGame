package ru.base.game.engine;

public interface Enemy extends Element {

    static Enemy empty() {
        return new Empty();
    }

    record Empty() implements Enemy {
        @Override
        public void tick() {
            //Nothing
        }
    }
}
