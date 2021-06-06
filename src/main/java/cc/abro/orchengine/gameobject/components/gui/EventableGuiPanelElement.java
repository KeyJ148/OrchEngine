package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;
import org.liquidengine.legui.component.Component;

import java.util.List;

public class EventableGuiPanelElement<T extends EventableGuiPanel>  extends EventableGuiElement<T>{

    public EventableGuiPanelElement(T component, List<GuiElementController> controllers) {
        super(component, controllers);
        component.addListener(this);
    }

    public EventableGuiPanelElement(T component, List<GuiElementController> controllers, boolean moveComponentToGameObjectPosition) {
        super(component, controllers, moveComponentToGameObjectPosition);
        component.addListener(this);
    }

    public EventableGuiPanelElement(T component, List<GuiElementController> controllers, int weight, int height) {
        super(component, controllers, weight, height);
        component.addListener(this);
    }

    public EventableGuiPanelElement(T component, List<GuiElementController> controllers, boolean moveComponentToGameObjectPosition, int weight, int height) {
        super(component, controllers, moveComponentToGameObjectPosition, weight, height);
        component.addListener(this);
    }
}
