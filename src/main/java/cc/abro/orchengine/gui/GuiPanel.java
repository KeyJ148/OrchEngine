package cc.abro.orchengine.gui;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.services.CachedGuiElementService;
import cc.abro.orchengine.services.GuiElementService;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;

import java.util.function.Consumer;

/**
 * Абстрактный класс от которого наследуются классы готовых панелей. Панель используется для группирования элементов
 * интерфейса. Класс содержит общие функции упрощающие работу всем панелям.
 *
 */
public abstract class GuiPanel extends Panel {

    /**
     * Инициализация панели с элементами интерфейса. Должна вызываться из самого дочернего элемента в конструкторе,
     * чтобы инициализировать стандартные стили.
     */
    public abstract void init();

    public void addComponent(Component component, int x, int y, int width, int height) {
        component.setPosition(x, y);
        component.setSize(width, height);
        add(component);
    }

    public void addComponentAndShiftedCenterToPoint(Component component, int x, int y, int width, int height) {
        component.setPosition(x - width / 2, y - height / 2);
        component.setSize(width, height);
        add(component);
    }

    public void lockAllChildComponents(){
        lockAllChildComponents(this);
    }

    private static void lockAllChildComponents(Component component){
        component.setFocusable(false);
        component.getChildComponents().iterator().forEachRemaining(GuiPanel::lockAllChildComponents);
    }
}
