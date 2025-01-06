package ru.base.game.engine.lang;

import java.util.ArrayList;
import java.util.List;

public interface VirtualMachine extends Program {

    boolean execute();

    void add(Command.Instance... commands);

    void reset();

    final class Default implements VirtualMachine, Evaluator {
        private final Evaluator evaluator;
        private final List<Command.Instance> commands = new ArrayList<>();
        private int index;

        public Default(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public Default(Object target) {
            this(new ObjectEvaluator(target));
        }

        @Override
        public int evaluate(Command.Instance instance) {
            return evaluate(instance, evaluator);
        }

        private int evaluate(Command.Instance instance, Evaluator evaluator) {
            Command command = instance.command();
            Argument[] args = instance.args();
            if (command.kind == Command.Kind.FLOW) {
                switch (command) {
                    case FORWARD -> {
                        if (args.length == 1 && args[0] instanceof Argument.Numeric delta) {
                            return instance.index() + delta.number().intValue();
                        }
                        throw new UnsupportedOperationException();
                    }
                    case BACKWARD -> {
                        if (args.length == 1 && args[0] instanceof Argument.Numeric delta) {
                            return instance.index() - delta.number().intValue();
                        }
                        throw new UnsupportedOperationException();
                    }
                    default -> {
                        return instance.index() + 1;
                    }
                }
            }
            return evaluator.evaluate(instance);
        }

        @Override
        public boolean execute(Evaluator evaluator) {
            if (commands.isEmpty() || index >= commands.size() || index < 0) {
                reset();
                return false;
            }
            Command.Instance instance = commands.get(index);
            index = evaluate(instance, evaluator);
            return true;
        }

        @Override
        public boolean execute() {
            return execute(evaluator);
        }

        @Override
        public void add(Command.Instance... commands) {
            this.commands.addAll(List.of(commands));
        }

        @Override
        public void reset() {
            index = 0;
            commands.clear();
        }
    }
}
