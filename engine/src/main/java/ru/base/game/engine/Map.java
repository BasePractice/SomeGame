package ru.base.game.engine;

import com.google.gson.annotations.SerializedName;
import ru.base.game.engine.map.StandardMapGenerator;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("PMD.UnusedPrivateMethod")
public interface Map extends Element {

    <E> E at(int x, int y, Layer layer);

    <E> List<Coordinated<E>> matrix(Layer layer);

    Coordinated<Matrix> matrix(int x, int y, int radius);

    void set(int x, int y, Layer layer, Object value);

    static Generator generator() {
        return new StandardMapGenerator();
    }

    String toString(int x, int y);

    enum Layer {
        BLOCKS, ITEMS, ENEMIES, VISIBLE, EVENTS, PATHS;

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

    enum PathMarker {
        MARKER
    }

    record Coordinated<E>(int x, int y, E source) {
    }

    final class MapElement {
        @SerializedName("layers")
        private final Object[] layers;

        public MapElement(int size) {
            this.layers = new Object[size];
        }

        private void set(int index, Object value) {
            Objects.checkIndex(index, layers.length);
            layers[index] = value;
        }
    }

    final class Matrix {
        private final MapElement[][] elements;

        public Matrix(int size) {
            this.elements = new MapElement[size][size];
        }

        public MapElement at(int x, int y) {
            return elements[y][x];
        }

        public void set(int x, int y, MapElement element) {
            elements[y][x] = element;
        }

        public int height() {
            return elements.length;
        }

        public int width() {
            return elements[0].length;
        }
    }
}
