package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.gui.EventableGuiElement;
import cc.abro.orchengine.gameobject.components.gui.GuiElementEvent;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class EventableGuiPanel extends GuiPanel{

    private final List<EventableGuiElement<?>> listeners = new LinkedList<>();

    public void addListener(EventableGuiElement<?> listener){
        listeners.add(listener);
    }

    public void removeListener(EventableGuiElement<?> listener){
        listeners.remove(listener);
    }

    protected void notifyAboutEvent(GuiElementEvent event){
        listeners.forEach(l -> l.callEvent(event));
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

    protected MouseClickEventListener getMouseReleaseListenerToNotify(Supplier<GuiElementEvent> eventSupplier){
        return getMouseReleaseListener(action -> notifyAboutEvent(eventSupplier.get()));
    }

    protected MouseClickEventListener getMouseReleaseListenerToNotifyEvents(List<GuiElementEvent> events){
        return getMouseReleaseListener(action -> events.forEach(this::notifyAboutEvent));
    }

    protected MouseClickEventListener getMouseReleaseListenerToNotifyEvents(Supplier<List<GuiElementEvent>> events){
        return getMouseReleaseListener(action -> events.get().forEach(this::notifyAboutEvent));
    }
}
