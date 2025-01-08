package ru.base.game.engine;

public interface Item extends Element {

    int damage();

    static Item empty() {
        return new Empty();
    }

    record Empty() implements Item {
        @Override
        public void tick() {
            //Nothing
        }

        @Override
        public int damage() {
            return 0;
        }
    }
}
