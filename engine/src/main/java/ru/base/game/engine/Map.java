package ru.base.game.engine;

import java.util.Objects;

@SuppressWarnings("PMD.UnusedPrivateMethod")
public interface Map {

    <E> E at(int x, int y, Layer layer);

    void set(int x, int y, Layer layer, Object value);

    enum Layer {
        BLOCKS, ITEMS, VISITED;

        @SuppressWarnings("unchecked")
        private <E> E at(Element element) {
            return (E) element.layers[ordinal()];
        }

        private void set(Element element, Object value) {
            element.layers[ordinal()] = value;
        }

        static final class Element {
            private final Object[] layers;

            Element(int size) {
                this.layers = new Object[size];
            }

            private void set(int index, Object value) {
                Objects.checkIndex(index, layers.length);
                layers[index] = value;
            }
        }
    }

    final class Default implements Map {
        private final Layer.Element[][] elements;

        public Default(int width, int height) {
            this.elements = new Layer.Element[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Layer.BLOCKS.set(elements[i][j], BlockType.EMPTY);
                }
            }
        }

        @Override
        public <E> E at(int x, int y, Layer layer) {
            return layer.at(elements[x][y]);
        }

        @Override
        public void set(int x, int y, Layer layer, Object value) {
            layer.set(elements[x][y], value);
        }
    }

    enum BlockType {
        EMPTY, WALL
    }

    enum ElementType {
        ENEMY, ITEM, EVENT, ENTER, EXIT
    }
}
