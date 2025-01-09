package ru.base.game.engine.events;

import ru.base.game.engine.Context;

public final class Exit extends StandardEvent {
    public Exit(Object source) {
        super(Type.EXIT, source);
    }

    @Override
    public boolean activate(Context context) {
        context.nextLevel();
        return false;
    }
}
