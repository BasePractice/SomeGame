package ru.base.game.engine;

import ru.base.game.engine.map.LevelMapGenerator;

import java.util.Objects;

@SuppressWarnings("PMD.UnusedPrivateMethod")
public interface Map extends Element {

    <E> E at(int x, int y, Layer layer);

    void set(int x, int y, Layer layer, Object value);

    static Generator generator() {
        return new LevelMapGenerator();
    }

    enum Layer {
        BLOCKS, ITEMS, ENEMIES, VISIBLE, EVENTS;

        @SuppressWarnings("unchecked")
        public <E> E at(MapElement element) {
            return (E) element.layers[ordinal()];
        }

        public void set(MapElement element, Object value) {
            element.set(ordinal(), value);
        }

    }

    @FunctionalInterface
    interface Generator {
        Map generate(int level, int width, int height, Kind kind);

        enum Kind {
            D1D, D2D
        }
    }

    int width();

    int height();

    enum BlockType {
        EMPTY, WALL
    }

    final class MapElement {
        private final Object[] layers;

        public MapElement(int size) {
            this.layers = new Object[size];
        }

        private void set(int index, Object value) {
            Objects.checkIndex(index, layers.length);
            layers[index] = value;
        }
    }
}
