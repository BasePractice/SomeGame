package ru.base.game.engine.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@FunctionalInterface
public interface Evaluator {
    int evaluate(Command.Instance instance);

    final class ObjectEvaluator implements Evaluator {
        private final Map<String, Method> methods = new HashMap<>();
        private final ObjectTarget object;

        public ObjectEvaluator(ObjectTarget object) {
            this.object = object;
        }

        private static Object[] transformArguments(Argument[] arguments) {
            Object[] result = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] instanceof Argument.Numeric argument) {
                    result[i] = argument.number();
                }
            }
            return result;
        }

        @Override
        public int evaluate(Command.Instance instance) {
            Command command = instance.command();
            Object[] args = transformArguments(instance.args());
            String methodName = command.name().toLowerCase(Locale.ROOT);
            Method method = methods.computeIfAbsent(methodName, name -> searchMethod(name, args));
            try {
                object.before(instance);
                Object result = method.invoke(object, args);
                object.after(instance, result);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return instance.index() + 1;
        }

        @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
        private Method searchMethod(String name, Object[] args) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (method.getName().equals(name)) {
                    if (method.getParameterCount() == args.length) {
                        method.setAccessible(true);
                        return method;
                    }
                }
            }
            throw new UnsupportedOperationException(name);
        }
    }

    interface ObjectTarget {
        default void after(Command.Instance instance, Object result) {
            //Nothing
        }

        default void before(Command.Instance instance) {
            //Nothing
        }
    }
}
