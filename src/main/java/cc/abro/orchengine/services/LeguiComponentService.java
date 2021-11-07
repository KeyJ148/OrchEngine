package cc.abro.orchengine.services;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.gui.GuiElement;
import cc.abro.orchengine.location.Location;
import org.liquidengine.legui.component.Component;

public class LeguiComponentService {

    private final GuiElementService guiElementService;

    public LeguiComponentService(GuiElementService guiElementService){
        this.guiElementService = guiElementService;
    }

    /**
     * Добавляет компонент на локацию. Координаты необходимо заранее задать в компоненте.
     * Для компонента создастся обертка в виде стандартного {@link GuiElement}.
     */
    public GameObject addComponentToLocationShiftedToCenter(Component component, Location location) {
        GuiElement<?> guiElement = new GuiElement<>(component);
        return guiElementService.addGuiElementToLocationShiftedToCenter(guiElement, location);
    }

    /**
     * Добавляет компонент на локацию в указанные координаты. Левый верхний угол компоненты будет находится в
     * переданных координатах. Для компонента создастся обертка в виде стандартного {@link GuiElement}.
     */
    public GameObject addComponentToLocation(Component component, int x, int y, Location location) {
        GuiElement<?> guiElement = new GuiElement<>(component);
        return guiElementService.addGuiElementToLocation(guiElement, x, y, location);
    }

    /**
     * Добавляет компонент на локацию в указанные координаты. Центр компоненты будет находится в
     * переданных координатах. Для компонента создастся обертка в виде стандартного {@link GuiElement}.
     */
    public GameObject addComponentToLocationShiftedToCenter(Component component, int x, int y, Location location) {
        GuiElement<?> guiElement = new GuiElement<>(component);
        return guiElementService.addGuiElementToLocationShiftedToCenter(guiElement, x, y, location);
    }

    /**
     * Добавляет компонент в центр локации. Центр компоненты будет находится в
     * центре локации. Для компонента создастся обертка в виде стандартного {@link GuiElement}.
     */
    public GameObject addComponentOnLocationCenter(Component component, Location location) {
        GuiElement<?> guiElement = new GuiElement<>(component);
        return guiElementService.addGuiElementOnLocationCenter(guiElement, location);
    }
}
