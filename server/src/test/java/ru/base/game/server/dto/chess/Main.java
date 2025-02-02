package ru.base.game.server.dto.chess;

import java.io.IOException;

public abstract class Main {
    public static void main(String[] args) throws IOException {
        PortableGameNotation notation =
            new PortableGameNotation.Default(Main.class.getResourceAsStream("/chess/1992.11.04.pgn"));
        System.out.println(notation);
    }
}
