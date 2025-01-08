package ru.base.game.engine.lang;

import ru.base.game.engine.DataManipulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

public interface Program {

    boolean execute(Evaluator evaluator);

    static DataManipulation<Program> manipulator(String fileName) {
        return new DefaultDataManipulation(fileName);
    }

    final class Default implements Program, Argument {
        private final Command.Instance[] instructions;
        private int index = 0;

        Default(Command.Instance... instructions) {
            this.instructions = instructions;
        }

        @Override
        public boolean execute(Evaluator evaluator) {
            if (hasNext()) {
                var inst = instructions[index];
                index = evaluator.evaluate(inst);
                return true;
            }
            return false;
        }

        boolean hasNext() {
            return index < instructions.length && index >= 0;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Default aDefault && Objects.deepEquals(instructions, aDefault.instructions);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(instructions);
        }
    }

    final class DefaultDataManipulation implements DataManipulation<Program> {
        private final File dataFile;

        public DefaultDataManipulation(String fileName) {
            this.dataFile = new File(fileName);
        }

        @Override
        public void write(Program program) {
            if (program instanceof Default def) {
                if (dataFile.exists()) {
                    dataFile.delete();
                }
                try (Writer writer = new FileWriter(dataFile)) {
                    Command.Instance[] instructions = def.instructions;
                    for (int i = 0; i < instructions.length; i++) {
                        Command.Instance instance = instructions[i];
                        if (i > 0) {
                            writer.write(";");
                        }
                        writer.write(instance.toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public Program read() {
            try {
                String text = Files.readString(dataFile.toPath());
                return new Default(new Parser.Default(text).parse());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
