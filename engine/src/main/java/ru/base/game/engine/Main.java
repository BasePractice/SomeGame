package ru.base.game.engine;

@SuppressWarnings("PMD.LooseCoupling")
public abstract class Main {
    public static void main(String[] args) {
        Game game = new Game(System.out::println);
        int count = 150;
        while (!game.isGameOver() && count > 0) {
            game.addCommand("right");
            game.tick();
            --count;
        }
    }
}
