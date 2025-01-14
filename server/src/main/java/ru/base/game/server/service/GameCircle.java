package ru.base.game.server.service;

import ru.base.game.engine.Game;
import ru.base.game.engine.Listener;
import ru.base.game.engine.Map;
import ru.base.game.engine.Player;
import ru.base.game.server.dto.EventObject;
import ru.base.game.server.dto.RefreshObject;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.EmptyCatchBlock"})
public final class GameCircle implements Runnable, Listener {
    final String username;
    final Game game;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final UserGameService userGameService;
    private LocalDateTime lastCommand = LocalDateTime.now();

    public GameCircle(String username, UserGameService userGameService) {
        this.username = username;
        this.userGameService = userGameService;
        this.game = new Game(this);
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

    @Override
    public <E> void emit(Event<E> event) {
        if (event instanceof Refresh refresh) {
            Player player = refresh.player();
            Map.Coordinated<Map.Matrix> matrix = refresh.data().matrix(player.x(), player.y(), player.visibleRadius());
            userGameService.sendEvent(username, new EventObject<>("refresh", new RefreshObject(player, matrix.source())));
        }
    }
}
