package ru.base.game.engine;

import ru.base.game.engine.items.DamagedItem;
import ru.base.game.engine.lang.Command;
import ru.base.game.engine.lang.Evaluator;
import ru.base.game.engine.lang.Parser;
import ru.base.game.engine.lang.VirtualMachine;
import ru.base.game.engine.map.StandardMapGenerator;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"PMD.UnusedPrivateMethod", "PMD.FinalFieldCouldBeStatic"})
public final class Game implements Evaluator.ObjectTarget, Context {
    private final VirtualMachine vm = new VirtualMachine.Default(this);
    private final AtomicInteger level = new AtomicInteger(1);
    private final Map.Generator generator = new StandardMapGenerator();
    private final int width = 20;
    private final int height = 20;
    private final Player player = new Player();
    private boolean userAttacked = false;
    private Map map;
    private State state = State.INITIATE;

    public void addCommand(String text) {
        Command.Instance[] parsed = new Parser.Default(text).parse();
        vm.add(parsed);
    }

    public void tick() {
        switch (state) {
            case INITIATE: {
                change(State.NEXT_LEVEL);
                break;
            }
            case BATTLE:
            case RUNNING: {
                player.tick(this);
                map.tick(this);
                vm.execute();
                break;
            }
            case NEXT_LEVEL: {
                userAttacked = false;
                player.flyTo(0, 0);
                map = generator.generate(level.getAndIncrement(), width, height, Map.Generator.Kind.D1D);
                map.list(Map.Layer.EVENTS).stream()
                    .filter(co -> co.source() instanceof Event e && e.type() == Event.Type.ENTER)
                    .findAny().ifPresent(c -> player.flyTo(c.x(), c.y()));
                change(State.RUNNING);
                System.out.println(map);
                break;
            }
            default: {
                break;
            }
        }
    }

    private void change(State state) {
        if (this.state != state) {
            System.out.printf("%s -> %s%n", this.state, state);
        }
        this.state = state;
    }

    private void left() {
        actionTo(player.x - 1, player.y);
        System.out.println("left");
    }

    private void right() {
        actionTo(player.x + 1, player.y);
        System.out.println("right");
    }

    private void top() {
        actionTo(player.x, player.y + 1);
        System.out.println("top");
    }

    private void bottom() {
        actionTo(player.x, player.y - 1);
        System.out.println("bottom");
    }

    private void actionTo(int x, int y) {
        Map.BlockType bt = map.at(x, y, Map.Layer.BLOCKS);
        if (bt == Map.BlockType.WALL || state == State.GAME_OVER || state == State.NEXT_LEVEL) {
            return;
        }
        Enemy enemy = map.at(x, y, Map.Layer.ENEMIES);
        Event event = map.at(x, y, Map.Layer.EVENTS);
        boolean visible = map.at(x, y, Map.Layer.VISIBLE);
        if (visible) {
            if (enemy == null) {
                if (state == State.BATTLE) {
                    System.out.println("We in battle mode. Can't move");
                    return;
                }
                player.flyTo(x, y);
                if (event != null) {
                    boolean mustRemoved = event.activate(this);
                    if (mustRemoved) {
                        map.set(x, y, Map.Layer.EVENTS, null);
                    }
                }
            } else {
                change(State.BATTLE);
                if (userAttacked) {
                    //Пользователь уже атаковал Enemy
                    return;
                }
                userAttacked = true;
                enemy.attack(player.selectedItem);
                if (enemy.isDead()) {
                    map.set(x, y, Map.Layer.ENEMIES, null);
                    Enemy.Bonus bonus = enemy.dieBonus();
                    if (bonus != null) {
                        bonus.apply(player);
                    }
                    change(State.RUNNING);
                    userAttacked = false;
                }
            }
        } else {
            map.set(x, y, Map.Layer.VISIBLE, true);
            if (enemy != null) {
                if (enemy.battleOn()) {
                    change(State.BATTLE);
                }
            }
        }
    }

    private void waiting() {
        System.out.println("waiting");
    }

    private void took() {
        DamagedItem item = map.at(player.x, player.y, Map.Layer.ITEMS);
        if (item != null) {
            boolean success = player.took(item);
            if (success) {
                map.set(player.x, player.y, Map.Layer.ITEMS, null);
            }
        }
        System.out.println("took");
    }

    private void select(int itemIndex) {
        player.selectItem(itemIndex);
        System.out.println("select: " + itemIndex);
    }

    private void loot(int price) {
        System.out.println("loot: " + price);
    }

    private void debug() {
        System.out.println("debug");
    }

    private void ping() {
        System.out.println("ping");
    }

    private void inventory() {
        System.out.println("inventory");
    }

    private void map() {
        System.out.println("map");
    }

    private void wallet() {
        System.out.println("wallet");
    }

    public boolean isGameOver() {
        return state == State.GAME_OVER;
    }

    @Override
    public void after(Command.Instance instance, Object result) {
        Command command = instance.command();
        if (command == Command.LEFT || command == Command.RIGHT || command == Command.BOTTOM || command == Command.TOP) {
            System.out.println(map.toString(player.x, player.y));
        }
    }

    @Override
    public void attack(DamagedItem selectedItem) {
        player.hit(selectedItem.damage());
        userAttacked = false;
        if (player.isDead()) {
            change(State.GAME_OVER);
        } else {
            System.out.println("Health: " + player.health);
        }
    }

    @Override
    public void nextLevel() {
        state = State.NEXT_LEVEL;
    }

    @Override
    public boolean canAttack() {
        return userAttacked;
    }

    private enum State {
        INITIATE, NEXT_LEVEL, BATTLE, RUNNING, GAME_OVER
    }
}
