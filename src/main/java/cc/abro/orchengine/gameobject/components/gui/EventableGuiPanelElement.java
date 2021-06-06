package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;
import org.liquidengine.legui.component.Component;

import java.util.List;
import java.util.Set;

public class EventableGuiPanelElement<T extends EventableGuiPanel>  extends EventableGuiElement<T>{

    public EventableGuiPanelElement(T component, Set<GuiElementController> controllers) {
        super(component, controllers);
        component.addListener(this);
    }

    public EventableGuiPanelElement(T component, Set<GuiElementController> controllers, boolean moveComponentToGameObjectPosition) {
        super(component, controllers, moveComponentToGameObjectPosition);
        component.addListener(this);
    }
}
