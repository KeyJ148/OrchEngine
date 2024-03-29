package cc.abro.orchengine.services;

import cc.abro.orchengine.context.GameService;
import cc.abro.orchengine.cycle.Render;
import org.joml.Vector2f;
import org.liquidengine.legui.component.Component;

@GameService
public class GuiService {

    private final Render render;

    public GuiService(Render render) {
        this.render = render;
    }

    public Vector2f getWindowCenter() {
        return new Vector2f(render.getWidth()/2, render.getHeight()/2);
    }

    public void moveComponentToWindowCenter(Component component) {
        component.setPosition(getWindowCenterForComponentCenter(component));
    }

    public Vector2f getWindowCenterForComponentCenter(Component component) {
        return new Vector2f(render.getWidth()/2 - component.getSize().x/2, render.getHeight()/2 - component.getSize().y/2);
    }

    public void replaceComponent(Component oldComponent, Component newComponent) {
        oldComponent.getFrame().getContainer().add(newComponent);
        oldComponent.getFrame().getContainer().remove(oldComponent);
    }
}
