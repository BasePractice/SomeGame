package ru.base.game.engine.map;

import ru.base.game.engine.Enemy;
import ru.base.game.engine.Event;
import ru.base.game.engine.Map;

public final class StandardMap implements Map {
    private final MapElement[][] elements;

    public StandardMap(int width, int height) {
        this.elements = new MapElement[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                elements[i][j] = new MapElement(Layer.values().length);
                Layer.BLOCKS.set(elements[i][j], BlockType.EMPTY);
                Layer.VISIBLE.set(elements[i][j], true);
            }
        }
    }

    @Override
    public <E> E at(int x, int y, Layer layer) {
        return layer.at(elements[y][x]);
    }

    @Override
    public void set(int x, int y, Layer layer, Object value) {
        layer.set(elements[y][x], value);
    }

    @Override
    public int width() {
        return elements[0].length;
    }

    @Override
    public int height() {
        return elements.length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (MapElement[] value : elements) {
            StringBuilder line = new StringBuilder();
            for (MapElement element : value) {
                if (!line.isEmpty()) {
                    line.append(" | ");
                }
                BlockType type = Layer.BLOCKS.at(element);
                boolean visible = Layer.VISIBLE.at(element);
                Object item = Layer.ITEMS.at(element);
                Enemy enemy = Layer.ENEMIES.at(element);
                Event event = Layer.EVENTS.at(element);
                if (type == BlockType.WALL) {
                    line.append("x");
                } else if (!visible) {
                    line.append("?");
                } else if (item != null) {
                    line.append("i");
                } else if (event != null) {
                    if (event.type() == Event.Type.ENTER) {
                        line.append(">");
                    } else if (event.type() == Event.Type.EXIT) {
                        line.append("<");
                    } else {
                        throw new IllegalArgumentException("Unknown event type: " + event);
                    }
                } else if (enemy != null) {
                    line.append("e");
                } else {
                    line.append(" ");
                }
            }
            builder.append(line).append('\n');
        }
        return builder.toString();
    }

    @Override
    public void tick() {
        for (MapElement[] element : elements) {
            for (MapElement mapElement : element) {
                Event event = Layer.EVENTS.at(mapElement);
                if (event != null) {
                    event.tick();
                }
                Enemy enemy = Layer.ENEMIES.at(mapElement);
                if (enemy != null) {
                    enemy.tick();
                }
            }
        }
    }
}
