package cc.abro.orchengine.gameobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ComponentsContainer extends Component {

    private Map<Class<? extends QueueComponent>, QueueComponent> components = new HashMap<>();
    private boolean destroy = false;

    public ComponentsContainer() {
        this(new ArrayList<>());
    }

    public ComponentsContainer(Collection<QueueComponent> initComponents) {
        for (QueueComponent component : initComponents) {
            setComponent(component);
        }
    }

    public void setComponent(QueueComponent component) {
        //TODO component.addToGameObject(this);
        components.put(component.getComponentClass(), component);
    }

    public <T extends QueueComponent> T getComponent(Class<T> classComponent) {
        return (T) components.get(classComponent);
    }

    public boolean hasComponent(Class<? extends QueueComponent> classComponent) {
        return getComponent(classComponent) != null;
    }

    public void removeComponent(Class<? extends QueueComponent> classComponent) {
        components.remove(classComponent);
    }

    @Override
    public void update(long delta) {
        for (QueueComponent component : components.values()) {
            component.startNewStep();
        }

        for (QueueComponent component : components.values()) {
            if (!isDestroy()) {
                component.update(delta);
            } else {
                component.destroy();
            }
        }
    }

    @Override
    public void draw() {
        for (QueueComponent component : components.values()) {
            component.draw();
        }
    }

    @Override
    public void destroy() {
        destroy = true;
        for (QueueComponent component : components.values()){
            component.destroy();
        }
    }

    public boolean isDestroy() {
        return destroy;
    }

    @Override
    public Class getComponentClass() {
        return null;
    }
}
