package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gameobject.components.Position;
import org.liquidengine.legui.component.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EventableGuiElement<T extends Component> extends GuiElement<T> {

    private final Map<Class<? extends GuiElementController>, GuiElementController> controllerByClass = new HashMap<>();

    public EventableGuiElement(T component, List<GuiElementController> controllers) {
        super(component);
        initControllers(controllers);
    }

    public EventableGuiElement(T component, List<GuiElementController> controllers,
                               boolean moveComponentToGameObjectPosition) {
        super(component, moveComponentToGameObjectPosition);
        initControllers(controllers);
    }

    public EventableGuiElement(T component, List<GuiElementController> controllers, int weight, int height) {
        super(component, weight, height);
        initControllers(controllers);
    }

    public EventableGuiElement(T component, List<GuiElementController> controllers,
                               boolean moveComponentToGameObjectPosition, int weight, int height) {
        super(component, moveComponentToGameObjectPosition, weight, height);
        initControllers(controllers);
    }

    private void initControllers(List<GuiElementController> controllers){
        for (GuiElementController controller : controllers){
            controller.init(this);
            controllerByClass.put(controller.getClass(), controller);
        }
    }

    public final void callEvent(GuiElementEvent event) {
        if (getGameObject().getComponent(Position.class).location.isActive()){
            if (controllerByClass.containsKey(event.getControllerClass())){
                controllerByClass.get(event.getControllerClass()).processEvent(event);
            }
        }
    }
}