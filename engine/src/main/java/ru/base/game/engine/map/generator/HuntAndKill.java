package ru.base.game.engine.map.generator;

import java.util.Date;
import java.util.Random;

final class HuntAndKill {
    private final Random random = new Random(new Date().getTime());
    private final int rows;
    private final int cols;
    private final char[][] data;
    private int fullness = 0;

    HuntAndKill(int rows, int cols) {
        this.rows = cols;
        this.cols = rows;
        data = new char[rows][cols];
    }

    public char getWalls(int x, int y) {
        return data[x][y];
    }

    public void fillMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = 0;
            }
        }
    }

    public void kill(int x, int y) {
        while ((x != 0 && data[x - 1][y] == 0) || (x != (rows - 1) && data[x + 1][y] == 0) ||
            (y != 0 && data[x][y - 1] == 0) || (y != (cols - 1) && data[x][y + 1] == 0)) {
            int c = random.nextInt(3) - 1;
            int d = random.nextInt(3) - 1;
            if (c == -1 && d == 0 && x != 0 && data[x + c][y + d] == 0) {
                data[x][y] -= 2;
                x -= 1;
                data[x][y] = 7;
            }
            if (c == 1 && d == 0 && x != (rows - 1) && data[x + c][y + d] == 0) {
                data[x][y] -= 8;
                x += 1;
                data[x][y] = 13;
            }
            if (c == 0 && d == -1 && y != 0 && data[x + c][y + d] == 0) {
                data[x][y] -= 1;
                y -= 1;
                data[x][y] = 11;
            }
            if (c == 0 && d == 1 && y != (cols - 1) && data[x + c][y + d] == 0) {
                data[x][y] -= 4;
                y += 1;
                data[x][y] = 14;
            }
        }
    }

    public void firstKill() {
        int x = random.nextInt(rows);
        int y = random.nextInt(cols);
        data[x][y] = 15;
        this.kill(x, y);
    }

    public void hunt() {
        while (fullness != rows * cols) {
            fullness = 0;
            a:
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if ((i != (rows - 1) && data[i][j] == 0 && data[i + 1][j] != 0) ||
                        (j != 0 && data[i][j - 1] != 0 && data[i][j] == 0) ||
                        (j != (cols - 1) && data[i][j] == 0 && data[i][j + 1] != 0) ||
                        (i != 0 && data[i - 1][j] != 0 && data[i][j] == 0)) {
                        if (i != (rows - 1) && data[i][j] == 0 && data[i + 1][j] != 0) {
                            data[i][j] = 7;
                            data[i + 1][j] -= 2;
                        }
                        if (j != 0 && data[i][j - 1] != 0 && data[i][j] == 0) {
                            data[i][j] = 14;
                            data[i][j - 1] -= 4;
                        }
                        if (j != (cols - 1) && data[i][j] == 0 && data[i][j + 1] != 0) {
                            data[i][j] = 11;
                            data[i][j + 1] -= 1;
                        }
                        if (i != 0 && data[i - 1][j] != 0 && data[i][j] == 0) {
                            data[i][j] = 13;
                            data[i - 1][j] -= 8;
                        }
                        this.kill(i, j);
                        break a;
                    }
                }
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (data[i][j] != 0) {
                        fullness++;
                    }
                }
            }
        }
    }
}
