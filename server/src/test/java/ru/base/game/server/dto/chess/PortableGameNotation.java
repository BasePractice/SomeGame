package ru.base.game.server.dto.chess;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface PortableGameNotation {

    enum Kind {
        KING, ROOK, BISHOP, KNIGHT, PAWN
    }

    enum Color {
        WHITE, BLACK
    }

    interface Element {
    }

    sealed interface Metadata extends Element {
        record Value(String name, String text) implements Metadata {

        }
    }

    sealed interface Move extends Element {
        enum Type {
            SIMPLE, CASTLING
        }

        record Line(int index, Step white, Step black) implements Move {

        }

        record Step(Kind kind,
                    Color color,
                    Optional<Position> from,
                    Optional<Position> to,
                    boolean capturing,
                    boolean checking,
                    Type type) {

        }
    }

    record Position(int x, int y) {

    }

    final class Default implements PortableGameNotation {
        private static final Pattern GRU = Pattern.compile(
            "(\\[(\\w+)\\s+\\\"(.*)\\\"\\]|\\d+\\.((([BQKNR]?([a-h1-8]?)x?[a-h][1-8]\\+?)|O-O|O-O-O)\\s(\\{(.*)\\}\\s)?){2})");
        private final Map<String, Metadata> metadata = new HashMap<>();
        private final List<Move> moves = new ArrayList<>();

        private Default(Reader reader) throws IOException {
            this(CharStreams.toString(reader));
        }

        private Default(String text) throws IOException {
            parse(text);
        }

        public Default(InputStream stream) throws IOException {
            this(new InputStreamReader(stream, StandardCharsets.UTF_8));
        }

        private void parse(String text) {
            metadata.clear();
            moves.clear();
            Matcher matcher = GRU.matcher(text);
            matcher.results().forEach(result -> {
                int grouped = result.groupCount();
                if (grouped > 0) {
                    String value = result.group(4);
                    if (value == null) {
                        metadata.put(result.group(2), new Metadata.Value(result.group(2), result.group(3)));
                    } else {
                        System.out.println("Found group '" + grouped + "'");
                        for (int i = 0; i < grouped; i++) {
                            System.out.printf("%d. %s%n", i, result.group(i));
                        }
                    }
                }
            });
        }
    }
}
