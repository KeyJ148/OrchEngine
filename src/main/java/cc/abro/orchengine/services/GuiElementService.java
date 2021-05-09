package cc.abro.orchengine.services;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.GameObjectFactory;
import cc.abro.orchengine.gameobject.components.gui.GuiElement;
import cc.abro.orchengine.map.Location;
import org.liquidengine.legui.component.Component;

public class GuiElementService {

    /**
     * Добавляет {@link GuiElement} на локацию. Координаты необходимо заранее задать
     * в компоненте (находящейся в {@link GuiElement}).
     */
    public GameObject addGuiElementToLocationShiftedToCenter(GuiElement<?> guiElement, Location location) {
        GameObject gameObject = GameObjectFactory.create();
        location.objAdd(gameObject);
        gameObject.setComponent(guiElement);
        return gameObject;
    }

    /**
     * Добавляет {@link GuiElement} на локацию в указанные координаты. Левый верхний угол компоненты
     * (находящейся в {@link GuiElement}) будет находится в переданных координатах.
     */
    public GameObject addGuiElementToLocation(GuiElement<?> guiElement, int x, int y, Location location) {
        guiElement.getComponent().setPosition(x, y);
        GameObject gameObject = GameObjectFactory.create();
        location.objAdd(gameObject);
        gameObject.setComponent(guiElement);
        return gameObject;
    }

    /**
     * Добавляет {@link GuiElement} на локацию в указанные координаты. Центр компоненты
     * (находящейся в {@link GuiElement}) будет находится в переданных координатах.
     */
    public GameObject addGuiElementToLocationShiftedToCenter(GuiElement<?> guiElement, int x, int y, Location location) {
        Component component = guiElement.getComponent();
        component.setPosition(x - component.getSize().x / 2, y - component.getSize().y / 2);
        GameObject gameObject = GameObjectFactory.create();
        location.objAdd(gameObject);
        gameObject.setComponent(guiElement);
        return gameObject;
    }

    /**
     * Добавляет {@link GuiElement} в центр локации. Центр компоненты (находящейся в {@link GuiElement})
     * будет находится в центре локации.
     */
    public GameObject addGuiElementOnLocationCenter(GuiElement<?> guiElement, Location location) {
        return addGuiElementToLocationShiftedToCenter(guiElement,
                Global.engine.render.getWidth() / 2,
                Global.engine.render.getHeight() / 2,
                location);
    }
}
