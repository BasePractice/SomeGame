package ru.base.game.engine;

import com.google.gson.annotations.SerializedName;

public interface Communication {


    record Command(@SerializedName("type") CommandType type,
                   @SerializedName("cmd") char cmd,
                   @SerializedName("args") Object... args) {
    }

    record Event(@SerializedName("kind") EventKind kind,
                 @SerializedName("args") Object... args) {

    }

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
    enum CommandType {
        CONTROL,
        DATA
    }

    enum EventKind {
        DEBUG,
        ERROR,
        MESSAGE,
        PONG
    }
}
