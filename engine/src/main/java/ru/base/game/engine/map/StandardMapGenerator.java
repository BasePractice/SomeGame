package ru.base.game.engine.map;

import ru.base.game.engine.Map;
import ru.base.game.engine.enemies.Human;
import ru.base.game.engine.events.Enter;
import ru.base.game.engine.events.Exit;
import ru.base.game.engine.items.DamagedItem;
import ru.base.game.engine.map.generator.DefaultMazeFactory;
import ru.base.game.engine.map.generator.MazeExtension;
import ru.base.game.engine.map.generator.MazeUtilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Random;

@SuppressWarnings("PMD.EmptyControlStatement")
public final class StandardMapGenerator implements Map.Generator {
    private final MazeExtension.Factory mazeFactory;
    private final Random random = new Random(new Date().getTime());

    public StandardMapGenerator() {
        this.mazeFactory = new DefaultMazeFactory();
    }

    @Override
    public Map generate(int level, int width, int height, Kind kind) {
        if (kind == Kind.D1D) {
            height = 3;
        }
        StandardMap map = new StandardMap(width, height);
        if (kind == Kind.D1D) {
            map.set(0, 1, Map.Layer.EVENTS, new Enter(this));
            map.set(width - 1, 1, Map.Layer.EVENTS, new Exit(this));
            for (int x = 0; x < width; x++) {
                map.set(x, 0, Map.Layer.BLOCKS, Map.BlockType.WALL);
                map.set(x, 2, Map.Layer.BLOCKS, Map.BlockType.WALL);
            }
        } else if (kind == Kind.D2D) {
            MazeExtension extension = mazeFactory.create(MazeExtension.Factory.Type.BTE);
            MazeExtension.Maze generated = extension.generateMaze(height / 2, width / 2);

            for (int x = 0; x < width; x++) {
                map.set(x, 0, Map.Layer.BLOCKS, Map.BlockType.WALL);
                map.set(x, height - 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
            }
            for (int y = 0; y < height; y++) {
                map.set(0, y, Map.Layer.BLOCKS, Map.BlockType.WALL);
                map.set(width - 1, y, Map.Layer.BLOCKS, Map.BlockType.WALL);
            }

            for (int y = 0; y < generated.cols; y++) {
                for (int x = 0; x < generated.rows; x++) {
                    var d = generated.data[y][x];
                    var xTo = y * 2 + 1;
                    var yTo = x * 2 + 1;
                    if ((d & MazeExtension.SQUARE_UP) == MazeExtension.SQUARE_UP) {
                        map.set(xTo - 1, yTo - 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
                        map.set(xTo, yTo - 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
                        map.set(xTo + 1, yTo - 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
                    } else if ((d & MazeExtension.SQUARE_LEFT) == MazeExtension.SQUARE_LEFT) {
                        map.set(xTo - 1, yTo - 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
                        map.set(xTo - 1, yTo, Map.Layer.BLOCKS, Map.BlockType.WALL);
                        map.set(xTo - 1, yTo + 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
                    } else if ((d & MazeExtension.SQUARE_RIGHT) == MazeExtension.SQUARE_RIGHT) {
                        map.set(xTo + 1, yTo - 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
                        map.set(xTo + 1, yTo, Map.Layer.BLOCKS, Map.BlockType.WALL);
                        map.set(xTo + 1, yTo + 1, Map.Layer.BLOCKS, Map.BlockType.WALL);
                    }
                }
            }
            map.set(1, 1, Map.Layer.EVENTS, new Enter(this));
            map.set(width - 1, height - 1, Map.Layer.EVENTS, new Exit(this));
            MazeExtension.Point[] path = extension.findPath(generated);
            int lxTo = -1;
            int lyTo = -1;
            for (MazeExtension.Point pt : path) {
                var xTo = pt.y() * 2 + 1;
                var yTo = pt.x() * 2 + 1;
                map.set(xTo, yTo, Map.Layer.PATHS, Map.PathMarker.MARKER);
                if (lxTo != -1) {
                    if (xTo == lxTo) {
                        map.set(xTo, yTo - 1, Map.Layer.PATHS, Map.PathMarker.MARKER);
                    } else if (yTo == lyTo) {
                        map.set(xTo - 1, yTo, Map.Layer.PATHS, Map.PathMarker.MARKER);
                    }
                }
                lxTo = xTo;
                lyTo = yTo;
            }
            try {
                BufferedImage image = MazeUtilities.createImage(generated, path);
                ImageIO.write(image, "PNG", new File(String.format("2D.Level-%d.png", level)));
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        int items = random.nextInt(3) + 5;
        int enemies = random.nextInt(7) + 2;
        for (int i = 0; i < items; i++) {
            generateElement(map, Map.Layer.ITEMS);
        }
        for (int i = 0; i < enemies; i++) {
            generateElement(map, Map.Layer.ENEMIES);
        }
        return map;
    }

    private void generateElement(Map map, Map.Layer layer) {
        int x = 0;
        int y = 0;
        while (true) {
            var bt = map.at(x, y, Map.Layer.BLOCKS);
            var event = map.at(x, y, Map.Layer.EVENTS);
            var item = map.at(x, y, Map.Layer.ITEMS);
            if (bt == Map.BlockType.EMPTY && event == null && item == null) {
                break;
            }
            x = random.nextInt(map.width() - 1);
            y = random.nextInt(map.height() - 1);
        }
        map.set(x, y, Map.Layer.VISIBLE, false);
        switch (layer) {
            case ITEMS: {
                map.set(x, y, Map.Layer.ITEMS, new DamagedItem(10));
                break;
            }
            case ENEMIES: {
                map.set(x, y, Map.Layer.ENEMIES, new Human(random.nextInt(90) + 20));
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
}
