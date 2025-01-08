package ru.base.game.engine.lang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.base.game.engine.DataManipulation;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Program")
class ProgramTest {
    private static final String DEFAULT_FILE_NAME = "program.some";

    @AfterEach
    void tearDown() {
        //assertTrue(new File(DEFAULT_FILE_NAME).delete());
    }

    @DisplayName("manipulator")
    @Test
    void manipulator() {
        Program program = new Program.Default(
            new Parser.Default("ping;ping;ping").parse()
        );
        DataManipulation<Program> manipulator = Program.manipulator(DEFAULT_FILE_NAME);
        manipulator.write(program);
        Program program2 = manipulator.read();
        assertEquals(program, program2);
    }
}
