package ru.base.game.engine.lang;

public interface Program {

    boolean execute(Evaluator evaluator);

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
    }
}
