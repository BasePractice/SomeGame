package ru.base.game.engine.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Evaluator")
class EvaluatorTest {

    @DisplayName("Object")
    @Test
    void objectEvaluator() {
        final boolean[] left = new boolean[]{false};
        final int[] loot = new int[]{0};
        Evaluator evaluator = new Evaluator.ObjectEvaluator(new Evaluator.ObjectTarget() {
            @Override
            public void after(Command.Instance instance, Object result) {
                //Nothing
            }

            private void left() {
                left[0] = true;
            }

            private void loot(int number) {
                loot[0] = number;
            }
        });
        evaluator.evaluate(new Command.Instance(0, Command.LEFT));
        evaluator.evaluate(new Command.Instance(0, Command.LOOT, new Argument.Numeric(10)));
        assertTrue(left[0]);
        assertEquals(10, loot[0]);
    }
}
