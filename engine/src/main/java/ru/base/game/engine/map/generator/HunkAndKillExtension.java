package ru.base.game.engine.map.generator;

final class HunkAndKillExtension implements MazeExtension {
    @Override
    public Maze generateMaze(int rows, int cols) {
        final Maze maze = new Maze(rows, cols);
        HuntAndKill hak = new HuntAndKill(rows, cols);
        hak.fillMaze();
        hak.firstKill();
        hak.hunt();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze.data[i][j] = hak.getWalls(j, i);
            }
        }
        maze.data[0][0] -= 1;
        maze.data[rows - 1][cols - 1] -= 4;
        return maze;
    }

    @Override
    public Point[] findPath(Maze maze) {
        return new Point[0];
    }
}
