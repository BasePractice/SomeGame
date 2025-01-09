package ru.base.game.engine.map.generator;

public interface MazeExtension {

    Maze generateMaze(int rows, int cols);

    Point[] findPath(Maze maze);

    @FunctionalInterface
    interface Factory {
        MazeExtension create(Type type);

        enum Type {
            HAK, BTE
        }
    }

    final class Maze {
        public final int cols;
        public final int rows;
        public final char[][] data;

        public Maze(int rows, int cols) {
            this.cols = cols;
            this.rows = rows;
            this.data = new char[rows][cols];
        }
    }

    record Point(int x, int y) {
    }

    char SQUARE_LEFT = 1;
    char SQUARE_UP = 2;
    char SQUARE_RIGHT = 4;
    char SQUARE_DOWN = 8;
}
