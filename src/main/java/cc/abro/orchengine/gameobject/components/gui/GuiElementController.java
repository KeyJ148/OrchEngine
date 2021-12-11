package cc.abro.orchengine.gameobject.components.gui;


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

    public abstract Class<T> getProcessedEventClass();

    public abstract void processEvent(T event);
}