package cc.abro.orchengine.gameobject;

import cc.abro.orchengine.location.Location;

import java.util.ArrayList;
import java.util.Collection;

import static cc.abro.orchengine.location.map.Map.LocationObjectHolder;

public class GameObject {

    private final LocationObjectHolder locationHolder = new LocationObjectHolder();
    private final ComponentsContainer components;
    private boolean destroying = false;
    private boolean destroyed = false;

    public GameObject() {
        this(new ArrayList<>());
    }

    public GameObject(Collection<Component> initComponents) {
        components = new ComponentsContainer(initComponents);
    }

    public void update(long delta) {
        components.update(delta);

        if (destroying) {
            components.destroy();
            getLocation().getMap().remove(this);
            destroying = false;
            destroyed = true;
        }
    }

    public void draw() {
        components.draw();
    }

    /**
     * The object will be destroyed only at the end of his update cycle
     */
    public void destroy() {
        if (!destroyed) {
            destroying = true;
        }
    }

    public boolean isDestroy() {
        return destroying || destroyed;
    }

    public Location getLocation() {
        return locationHolder.getLocation();
    }

    public LocationObjectHolder getLocationHolder() {
        return locationHolder;
    }

    /*
     * Просто прокси методы
     */
    public void setComponent(Component component) {
        components.setComponent(component);
    }

    public <T extends Component> T getComponent(Class<T> classComponent) {
        return components.getComponent(classComponent);
    }

    public boolean hasComponent(Class<? extends Component> classComponent) {
        return components.hasComponent(classComponent);
    }

    public void removeComponent(Class<? extends Component> classComponent) {
        components.removeComponent(classComponent);
    }
}