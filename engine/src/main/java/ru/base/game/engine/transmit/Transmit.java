package ru.base.game.engine.transmit;

import java.io.PrintStream;
import java.util.Scanner;

public interface Transmit extends Input, Output {

    final class Standard implements Transmit {
        private final PrintStream output;
        private final Scanner input;

        Standard(PrintStream output, Scanner input) {
            this.output = output;
            this.input = input;
        }

        public Standard() {
            this(new PrintStream(System.out), new Scanner(System.in));
        }

        @Override
        public String readText() {
            return input.nextLine();
        }

        @Override
        public void print(String format, Object... args) {
            output.printf(format, args);
            //NOTICE: Не очень хорошо
            output.flush();
        }
    }
}
