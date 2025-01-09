package ru.base.game.engine.map;

import ru.base.game.engine.Context;
import ru.base.game.engine.Enemy;
import ru.base.game.engine.Event;
import ru.base.game.engine.Map;

import java.util.ArrayList;
import java.util.List;

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
        if (y >= 0 && y < elements.length && x >= 0 && x < elements[y].length) {
            return layer.at(elements[y][x]);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> List<Coordinated<E>> list(Layer layer) {
        List<Coordinated<E>> list = new ArrayList<>();
        for (int y = 0; y < elements.length; y++) {
            for (int x = 0; x < elements[y].length; x++) {
                MapElement mapElement = elements[y][x];
                Object object = layer.at(mapElement);
                if (object != null) {
                    list.add(new Coordinated<>(x, y, (E) object));
                }
            }
        }
        return list;
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
    public String toString(int xPlayer, int yPlayer) {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < elements.length; y++) {
            MapElement[] elementsLine = elements[y];
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < elementsLine.length; x++) {
                MapElement element = elementsLine[x];
                if (!line.isEmpty()) {
                    line.append(" | ");
                }
                BlockType type = Layer.BLOCKS.at(element);
                boolean visible = Layer.VISIBLE.at(element);
                Object item = Layer.ITEMS.at(element);
                Enemy enemy = Layer.ENEMIES.at(element);
                Event event = Layer.EVENTS.at(element);
                if (x == xPlayer && y == yPlayer) {
                    line.append("P");
                } else if (type == BlockType.WALL) {
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
    public String toString() {
        return toString(-1, -1);
    }

    @Override
    public void tick(Context context) {
        for (MapElement[] element : elements) {
            for (MapElement mapElement : element) {
                Event event = Layer.EVENTS.at(mapElement);
                if (event != null) {
                    event.tick(context);
                }
                Enemy enemy = Layer.ENEMIES.at(mapElement);
                if (enemy != null) {
                    enemy.tick(context);
                }
            }
        }
    }
}
