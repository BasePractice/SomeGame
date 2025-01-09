package ru.base.game.engine.map.generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

final class BinaryTreeExtension implements MazeExtension {
    private final Random random = new Random(new Date().getTime());

    @Override
    public Maze generateMaze(int rows, int cols) {
        Cell[][] cells = new Cell[rows][cols];

        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                Cell nc = new Cell(i, j);
                if (j == 0) {
                    nc.right = false;
                    nc.left = false;
                    nc.up = true;
                } else {
                    nc.up = true;
                    nc.down = true;
                    nc.right = true;
                    if (random.nextBoolean() || i == 0) {
                        nc.up = false;
                        nc.left = true;
                        cells[i][j - 1].down = false;
                    } else {
                        nc.left = false;
                        cells[i - 1][j].right = false;
                    }
                }

                if (j == rows - 1) {
                    nc.down = true;
                }

                if (i == cols - 1) {
                    nc.right = true;
                }

                cells[i][j] = nc;
            }
        }

        Maze maze = new Maze(rows, cols);
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                maze.data[x][y] = cells[x][y].getValue();
            }
        }

        return maze;
    }

    @Override
    public Point[] findPath(Maze maze) {
        Cell[][] cells = new Cell[maze.rows][maze.cols];
        Set<Cell> cellSet = new HashSet<>();
        for (int y = 0; y < maze.rows; y++) {
            for (int x = 0; x < maze.cols; x++) {
                Cell cell = new Cell(x, y, maze.data[x][y]);
                cells[x][y] = cell;
                cellSet.add(cell);
            }
        }

        Cell start = cells[0][0];
        start.distance = 0;
        Cell finish = cells[maze.rows - 1][maze.cols - 1];
        int wave = 0;
        do {
            for (Cell cell : cellSet) {
                if (cell.distance == wave) {
                    List<Cell> neighbors = cell.getNeighborCells(cellSet);
                    for (Cell neighbor : neighbors) {
                        if (neighbor.distance == -1) {
                            neighbor.distance = wave + 1;
                        }
                    }
                }
            }
            wave++;
        } while (finish.distance == -1);
        List<Cell> path = new ArrayList<>();
        path.add(finish);
        Cell currCell = finish;
        while (!path.contains(start)) {
            currCell = closest(Objects.requireNonNull(currCell).getNeighborCells(cellSet));
            path.add(currCell);
        }
        Point[] pathPoints = new Point[path.size()];
        for (int i = 0; i < pathPoints.length; i++) {
            pathPoints[i] = new Point(path.get(i).x, path.get(i).y);
        }

        return pathPoints;
    }

    private static Cell closest(List<Cell> neighbors) {
        neighbors.sort((o1, o2) -> {
            Integer d1 = o1.distance;
            Integer d2 = o2.distance;
            return d1.compareTo(d2);
        });
        for (Cell cell : neighbors) {
            if (cell.distance != -1) {
                return cell;
            }
        }
        return null;
    }

    private static final class Cell {
        final int x;
        final int y;
        long groupId;
        boolean up;
        boolean left;
        boolean down;
        boolean right;
        int distance;

        private Cell(int x, int y) {
            this.x = x;
            this.y = y;
            this.groupId = 0;
            this.up = false;
            this.left = false;
            this.down = false;
            this.right = false;
        }

        Cell(int x, int y, char c) {
            this.x = x;
            this.y = y;
            if ((c & MazeExtension.SQUARE_LEFT) == MazeExtension.SQUARE_LEFT) {
                this.left = true;
            }
            if ((c & MazeExtension.SQUARE_UP) == MazeExtension.SQUARE_UP) {
                this.up = true;
            }
            if ((c & MazeExtension.SQUARE_RIGHT) == MazeExtension.SQUARE_RIGHT) {
                this.right = true;
            }
            if ((c & MazeExtension.SQUARE_DOWN) == MazeExtension.SQUARE_DOWN) {
                this.down = true;
            }
            this.distance = -1;
        }

        char getValue() {
            char val = 0;
            if (up) {
                val |= MazeExtension.SQUARE_UP;
            }
            if (left) {
                val |= MazeExtension.SQUARE_LEFT;
            }
            if (down) {
                val |= MazeExtension.SQUARE_DOWN;
            }
            if (right) {
                val |= MazeExtension.SQUARE_RIGHT;
            }
            return val;
        }

        boolean isNeighborCell(Cell otherCell) {
            return Math.abs(this.x - otherCell.x) + Math.abs(this.y - otherCell.y) == 1;
        }

        boolean canMoveTo(Cell to) {
            if (this.isNeighborCell(to)) {
                switch (this.x - to.x) {
                    case 0: {
                        switch (this.y - to.y) {
                            case 1: {
                                if (!this.up && !to.down) {
                                    return true;
                                }
                                break;
                            }
                            case -1: {
                                if (!this.down && !to.up) {
                                    return true;
                                }
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    case 1: {
                        if (!this.left && !to.right) {
                            return true;
                        }
                        break;
                    }
                    case -1: {
                        if (!this.right && !to.left) {
                            return true;
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
            return false;
        }

        List<Cell> getNeighborCells(Set<Cell> cells) {
            return cells.stream().filter(cell2 ->
                this.isNeighborCell(cell2) &&
                    this.canMoveTo(cell2)).collect(Collectors.toList());
        }

    }
}
