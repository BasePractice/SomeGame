package ru.base.game.engine.map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.base.game.engine.Context;
import ru.base.game.engine.Enemy;
import ru.base.game.engine.Event;
import ru.base.game.engine.Map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class StandardMap implements Map {
    private final MapElement[][] elements;
    private final int width;
    private final int height;

    public StandardMap(int width, int height) {
        this.elements = new MapElement[height][width];
        this.width = width;
        this.height = height;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                elements[i][j] = new MapElement(Layer.values().length);
                Layer.BLOCKS.set(elements[i][j], BlockType.EMPTY);
                Layer.VISIBLE.set(elements[i][j], true);
            }
        }
    }

    public StandardMap(MapElement[][] elements) {
        this.elements = elements;
        this.width = elements[0].length;
        this.height = elements.length;
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
    public Coordinated<Matrix> matrix(int x, int y, int radius) {
        Matrix matrix = new Matrix(radius * 2);
        int ys = y - radius;
        if (ys < 0) {
            ys = 0;
        }
        int xs = x - radius;
        if (xs < 0) {
            xs = 0;
        }
        for (int yi = ys, yc = 0; yi >= 0 && yi < y + radius; yi++, yc++) {
            for (int xi = xs, xc = 0; xi >= 0 && xi < x + radius; xi++, xc++) {
                MapElement mapElement = elements[yi][xi];
                matrix.set(xc, yc, mapElement);
            }
        }
        return new Coordinated<>(xs, ys, matrix);
    }

    @Override
    public void set(int x, int y, Layer layer, Object value) {
        layer.set(elements[y][x], value);
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
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

    public static final class Adapter extends TypeAdapter<Map> {

        @Override
        public void write(JsonWriter out, Map value) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map read(JsonReader in) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}
