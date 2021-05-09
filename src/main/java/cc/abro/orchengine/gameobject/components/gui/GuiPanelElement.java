package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.gui.GuiPanel;
import cc.abro.orchengine.services.GuiElementService;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

import java.util.function.Consumer;

public abstract class GuiPanelElement<T extends Component> extends GuiElement<T> implements GuiElementEventListener {

    public GuiPanelElement(T component) {
        super(component);
    }

    public GuiPanelElement(T component, boolean moveComponentToGameObjectPosition) {
        super(component, moveComponentToGameObjectPosition);
    }

    public GuiPanelElement(T component, int weight, int height) {
        super(component, weight, height);
    }

    public GuiPanelElement(T component, boolean moveComponentToGameObjectPosition, int weight, int height) {
        super(component, moveComponentToGameObjectPosition, weight, height);
    }

    /* TODO
    public MouseClickEventListener getDestroyThisPanelMouseReleaseListener() {
        return getMouseReleaseListener(event -> destroy());
    }

    public MouseClickEventListener getCreateCachedPanelMouseReleaseListener(Class<? extends GuiPanel> newGuiPanelClass) {
        return getMouseReleaseListener(event -> createCachedPanel(newGuiPanelClass));
    }

    public MouseClickEventListener getChangeCachedPanelMouseReleaseListener(Class<? extends GuiPanel> newGuiPanelClass) {
        return getMouseReleaseListener(event -> {
            destroy();
            createCachedPanel(newGuiPanelClass);
        });
    }*/
}