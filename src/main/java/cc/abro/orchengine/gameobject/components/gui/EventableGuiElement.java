package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.gui.EventableGuiPanel;
import cc.abro.orchengine.gui.PanelControllersStorage;

public class EventableGuiElement<T extends EventableGuiPanel> extends GuiElement<T> {

    public EventableGuiElement(T component) {
        super(component);
        component.addListener(this);
    }

    public EventableGuiElement(T component, boolean moveComponentToGameObjectPosition) {
        super(component, moveComponentToGameObjectPosition);
        component.addListener(this);
    }

    public final void callEvent(GuiElementEvent event) {
        Manager.getService(PanelControllersStorage.class).processEvent(event, this);
    }

    @Override
    public void destroyAndCreateGuiElement(GuiElement<?> guiElement) {
        super.destroyAndCreateGuiElement(guiElement);
        component.removeListener();
    }
}