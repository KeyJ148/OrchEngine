package cc.abro.orchengine.init.interfaces;

public interface GameInterface {

    default void init() {} //Engine: Инициализация игры перед запуском главного цикла

    default void update(long delta) {} //Engine: Выполняется каждый степ перед обновлением всех игровых объектов

    default void render() {} //Engine: Выполняется каждый степ перед перерисовкой всех игровых объектов
}
