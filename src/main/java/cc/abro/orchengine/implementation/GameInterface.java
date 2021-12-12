package cc.abro.orchengine.implementation;

import java.util.Collections;
import java.util.List;

public interface GameInterface {

    default void init() {} //Engine: Инициализация игры перед запуском главного цикла

    //Engine: Выполняется каждый степ перед обновлением всех игровых объектов
    default List<Class<?>> getInitializingServices() {
        return Collections.emptyList();
    }

    default void update(long delta) {} //Engine: Выполняется каждый степ перед обновлением всех игровых объектов

    default void render() {} //Engine: Выполняется каждый степ перед перерисовкой всех игровых объектов


}
