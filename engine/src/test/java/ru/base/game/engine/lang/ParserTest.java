package ru.base.game.engine.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("Parser")
class ParserTest {

    private static Stream<Arguments> data() {
        return Stream.of(
            Arguments.of(new Command.Instance[]{
                new Command.Instance(0, Command.LEFT), new Command.Instance(1, Command.RIGHT),
            }, "left;right"),
            Arguments.of(new Command.Instance[]{
                new Command.Instance(0, Command.SELECT, new Argument.Numeric(1)),
            }, "select{1};"),
            Arguments.of(new Command.Instance[]{}, ";;;;;;;;;;;;")
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void parse(Command.Instance[] expected, String input) {
        Parser parser = new Parser.Default(input);
        Command.Instance[] parsed = parser.parse();
        assertArrayEquals(expected, parsed);
    }
}
