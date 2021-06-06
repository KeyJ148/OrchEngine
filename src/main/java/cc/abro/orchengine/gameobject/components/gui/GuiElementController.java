package cc.abro.orchengine.gameobject.components.gui;


import org.liquidengine.legui.component.Component;

public abstract class GuiElementController {

    private EventableGuiElement<?> guiElement;

    public void init(EventableGuiElement<?> guiElement){
        if (this.guiElement != null){
            throw new IllegalStateException();
        }
        this.guiElement = guiElement;
    }

    public EventableGuiElement<?> getGuiElement() {
        return guiElement;
    }

    protected abstract void processEvent(GuiElementEvent event);
}