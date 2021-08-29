package cc.abro.orchengine.gameobject.components.gui;


import org.liquidengine.legui.component.Component;

public abstract class GuiElementController<T extends GuiElementEvent> {

    private EventableGuiElement<?> guiElement;

    public void init(EventableGuiElement<?> guiElement){
        if (this.guiElement != null){
            throw new IllegalStateException("Object already was initialized");
        }
        this.guiElement = guiElement;
    }

    public EventableGuiElement<?> getGuiElement() {
        return guiElement;
    }

    public boolean isProcessedEvent(T event) {
        return getProcessedEventClass().equals(event.getClass());
    }

    protected abstract Class<T> getProcessedEventClass();

    protected abstract void processEvent(T event);
}