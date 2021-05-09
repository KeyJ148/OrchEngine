package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.gui.GuiElementEvent;
import cc.abro.orchengine.gameobject.components.gui.GuiElementEventListener;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.input.Mouse;
import org.liquidengine.legui.listener.MouseClickEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public abstract class EventableGuiPanel extends GuiPanel{

    private final List<GuiElementEventListener> listeners = new LinkedList<>();

    public void addListener(GuiElementEventListener listener){
        listeners.add(listener);
    }

    public void removeListener(GuiElementEventListener listener){
        listeners.remove(listener);
    }

    protected void notifyAboutEvent(GuiElementEvent event){
        listeners.forEach(l -> l.processEvent(event));
    }

    @SuppressWarnings("unchecked")
    protected MouseClickEventListener getMouseReleaseListener(Consumer<MouseClickEvent<Component>> mouseReleaseAction) {
        return event -> {
            event.getTargetComponent().setFocused(false);
            if (event.getAction() == MouseClickEvent.MouseClickAction.RELEASE) {
                mouseReleaseAction.accept(event);
            }
        };
    }

    protected MouseClickEventListener getMouseReleaseListenerToNotify(GuiElementEvent event){
        return getMouseReleaseListener(action -> notifyAboutEvent(event));
    }
}
