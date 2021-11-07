package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.QueueComponent;
import cc.abro.orchengine.gameobject.components.Movement;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.services.GuiElementService;
import cc.abro.orchengine.util.Vector2;
import org.liquidengine.legui.component.Component;

import java.util.Arrays;
import java.util.List;

public class GuiElement<T extends Component> extends QueueComponent {

    protected final T component;
    private boolean moveComponentToGameObjectPosition;

    public GuiElement(T component) {
        this(component, false);
    }

    public GuiElement(T component, boolean moveComponentToGameObjectPosition) {
        this.component = component;
        this.moveComponentToGameObjectPosition = moveComponentToGameObjectPosition;
    }

    public GuiElement(T component, int weight, int height) {
        this(component, false, weight, height);
    }

    public GuiElement(T component, boolean moveComponentToGameObjectPosition, int weight, int height) {
        this(component, moveComponentToGameObjectPosition);
        component.setSize(weight, height);
    }

    @Override
    public void addToGameObject(GameObject gameObject) {
        super.addToGameObject(gameObject);
        getGameObject().getComponent(Position.class).location.getGuiLocationFrame().getGuiFrame().getContainer().add(component);
    }

    //TODO на removeFromGameObject с возможность переносить компоненты между объектами?
    //TODO Проверить все вызовы этого и дочерних destroy
    @Override
    public void destroy() {
        getGameObject().getComponent(Position.class).location.getGuiLocationFrame().getGuiFrame().getContainer().remove(component);
    }

    @Override
    public void updateComponent(long delta) {
        if (moveComponentToGameObjectPosition) {
            Vector2<Integer> relativePosition = getGameObject().getComponent(Position.class).getRelativePosition();
            component.setPosition(relativePosition.x, relativePosition.y);
        }
    }

    @Override
    protected void drawComponent() {
    }

    @Override
    public List<Class<? extends QueueComponent>> getPreliminaryUpdateComponents() {
        return Arrays.asList(Movement.class);
    }

    @Override
    public List<Class<? extends QueueComponent>> getPreliminaryDrawComponents() {
        return Arrays.asList();
    }

    @Override
    public Class getComponentClass() {
        return GuiElement.class;
    }

    public T getComponent() {
        return component;
    }

    public boolean isMoveComponentToGameObjectPosition() {
        return moveComponentToGameObjectPosition;
    }

    public void setMoveComponentToGameObjectPosition(boolean moveComponentToGameObjectPosition) {
        this.moveComponentToGameObjectPosition = moveComponentToGameObjectPosition;
    }

    //TODO мб пересмотреть способ получения/передачи координат
    public Vector2<Double> getPosition() {
        double x = isMoveComponentToGameObjectPosition()?
                getGameObject().getComponent(Position.class).x : getComponent().getPosition().x + getComponent().getSize().x/2;
        double y = isMoveComponentToGameObjectPosition()?
                getGameObject().getComponent(Position.class).y : getComponent().getPosition().y + getComponent().getSize().y/2;
        return new Vector2<>(x, y);
    }

    //TODO уничтожать не объект целиком, а только GuiElement
    public void destroyAndCreateGuiElement(GuiElement<?> guiElement){
        Vector2<Double> position = getPosition();
        Manager.getService(GuiElementService.class).addGuiElementToLocationShiftedToCenter(guiElement,
                position.x.intValue(), position.y.intValue(),
                getGameObject().getComponent(Position.class).location);
        destroy();
    }
}
