package ru.base.game.engine.lang;

import java.util.Locale;
import java.util.Objects;

/**
 * <br>
 * 1. Управление
 * <br>
 * 1.1 - Action{Left,Right,Top,Bottom} - действие по 4 направлениям (верх, низ, лево, право). При этом
 * <br>
 * - Enemy - атака
 * <br>
 * - ?     - посмотреть что это, при этом вопросик становится или пустотой или Enemy или Item.
 * <br>
 * - Item  - перейти на клетку
 * <br>
 * - Пустой- перейти на клетку
 * <br>
 * 1.2 - Wait  - ожидание (в случае боя)
 * <br>
 * 1.3 - Catch - подобрать предмет
 * <br>
 * 1.4 - Select- выбрать предмет
 * <br>
 * 1.5 - Loot  - заказать случайную коробку с предметом
 * <br>
 * 1.6 - Debug - включает или отключает отладочную информацию с сервера
 * <br>
 * 1.7 - Ping - пингуем сервер, что мы живы
 * <br>
 * 2. Данные
 * <br>
 * 2.1 - Inventory - запросить инвентарь
 * <br>
 * 2.2 - Map - запросить карту
 * <br>
 * 2.3 - MapElements - запросить данные по карте?
 * <br>
 * 2.4 - Wallet - информация по кошельку пользователя
 * <br>
 */
public enum Command {
    UNKNOWN(0, (byte) 0x1F),
    LEFT(0, (byte) 0x20),
    RIGHT(0, (byte) 0x21),
    TOP(0, (byte) 0x22),
    BOTTOM(0, (byte) 0x23),
    WAITING(0, (byte) 0x24),
    TOOK(0, (byte) 0x25),
    SELECT(1, (byte) 0x26),
    LOOT(1, (byte) 0x27),
    DEBUG(0, (byte) 0x28),
    PING(0, (byte) 0x29),

    INVENTORY(Kind.DATA, 0, (byte) 0x50),
    MAP(Kind.DATA, 0, (byte) 0x51),
    MAP_ELEMENTS(Kind.DATA, 0, (byte) 0x52),
    WALLET(Kind.DATA, 0, (byte) 0x53),

    FORWARD(Kind.FLOW, 1, (byte) 0x90), // Сменить текущий указатель исполнения вперед на аргумент
    BACKWARD(Kind.FLOW, 1, (byte) 0x91),// То же самое только назад
    ;
    final Kind kind;
    final int argumentCount;
    final byte[] code;

    Command(Kind kind, int argumentCount, byte[] code) {
        this.kind = kind;
        this.argumentCount = argumentCount;
        this.code = code;
    }

    Command(int argumentCount, byte code) {
        this(Kind.CONTROL, argumentCount, code);
    }

    Command(Kind kind, int argumentCount, byte code) {
        this(kind, argumentCount, new byte[]{code});
    }

    static Command of(String text) {
        for (Command command : values()) {
            if (command.name().equalsIgnoreCase(text)) {
                return command;
            }
        }
        return UNKNOWN;
    }

    enum Kind {
        CONTROL,
        DATA,
        FLOW
    }

    public record Instance(int index, Command command, Argument... args) {
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Argument operand : args) {
                if (!builder.isEmpty()) {
                    builder.append(", ");
                }
                builder.append(operand);
            }
            return command.name().toLowerCase(Locale.ROOT) + (!builder.isEmpty() ? "{" + builder + "}" : "");
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Instance that && command == that.command;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(command);
        }
    }
}
