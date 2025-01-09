package ru.base.game.engine.map;

import ru.base.game.engine.Map;
import ru.base.game.engine.enemies.Human;
import ru.base.game.engine.events.Enter;
import ru.base.game.engine.events.Exit;
import ru.base.game.engine.items.DamagedItem;

import java.util.Date;
import java.util.Random;

@SuppressWarnings("PMD.EmptyControlStatement")
public final class StandardMapGenerator implements Map.Generator {
    private final Random random = new Random(new Date().getTime());

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
            //TODO: Генератор лабиринта
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
