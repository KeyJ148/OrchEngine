package cc.abro.orchengine.gameobject.components.gui;

import org.liquidengine.legui.component.Component;

import java.util.Set;

public abstract class EventableGuiElement<T extends Component> extends GuiElement<T> {

    private final Set<GuiElementController> controllers;

    public EventableGuiElement(T component, Set<GuiElementController> controllers) {
        super(component);
        this.controllers = controllers;
        initControllers(controllers);
    }

    public EventableGuiElement(T component, Set<GuiElementController> controllers,
                               boolean moveComponentToGameObjectPosition) {
        super(component, moveComponentToGameObjectPosition);
        this.controllers = controllers;
        initControllers(controllers);
    }

    private void initControllers(Set<GuiElementController> controllers){
        for (GuiElementController controller : controllers){
            controller.init(this);
        }
    }

    public final void callEvent(GuiElementEvent event) {
        if (getGameObject().getLocation().isActive()){
            for (GuiElementController controller : controllers){
                if (controller.isProcessedEvent(event)) {
                    controller.processEvent(event);
                }
            }
        }
    }
}