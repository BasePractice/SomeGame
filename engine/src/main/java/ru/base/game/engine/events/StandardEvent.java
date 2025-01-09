package ru.base.game.engine.events;

import ru.base.game.engine.Context;
import ru.base.game.engine.Event;

public class StandardEvent implements Event {
    private final Type type;
    private final Object source;
    protected int tickCount;

    public StandardEvent(Type type, Object source) {
        this.type = type;
        this.source = source;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public Object source() {
        return source;
    }

    @Override
    public boolean activate(Context context) {
        return false;
    }

    @Override
    public void tick(Context context) {
        ++tickCount;
    }
}
