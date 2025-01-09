package ru.base.game.engine.map.generator;

public final class DefaultMazeFactory implements MazeExtension.Factory {
    @Override
    public MazeExtension create(Type type) {
        switch (type) {
            case BTE -> {
                return new BinaryTreeExtension();
            }
            case HAK -> {
                return new HunkAndKillExtension();
            }
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
