package ru.base.game.server.service;

import ru.base.game.engine.Game;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameCircle implements Runnable {
    final String username;
    private final Game game = new Game();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final UserGameService userGameService;
    private LocalDateTime lastCommand = LocalDateTime.now();

    public GameCircle(String username, UserGameService userGameService) {
        this.username = username;
        this.userGameService = userGameService;
    }

    public void addCommand(String command) {
        game.addCommand(command);
        lastCommand = LocalDateTime.now();
    }

    public boolean isDead() {
        return LocalDateTime.now().minusMinutes(5).isAfter(lastCommand);
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        while (running.get()) {
            game.tick();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //None
            }
        }
    }
}
