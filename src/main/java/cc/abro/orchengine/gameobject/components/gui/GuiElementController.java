package cc.abro.orchengine.gameobject.components.gui;


public abstract class GuiElementController<T extends GuiElementEvent> {

    private GuiElement<?> guiElement;

    public void init(GuiElement<?> guiElement){
        if (this.guiElement != null){
            throw new IllegalStateException("Object already was initialized");
        }
        this.guiElement = guiElement;
    }

    public GuiElement<?> getGuiElement() {
        return guiElement;
    }

    public abstract Class<T> getProcessedEventClass();

    public abstract void processEvent(T event);
}