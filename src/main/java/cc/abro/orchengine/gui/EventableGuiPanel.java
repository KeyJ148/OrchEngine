package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.gui.EventableGuiElement;
import cc.abro.orchengine.gameobject.components.gui.GuiElementEvent;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class EventableGuiPanel extends GuiPanel{

    private EventableGuiElement<?> listener;

    //TODO может в конструктор?
    public void addListener(EventableGuiElement<?> listener){
        this.listener = listener;
    }

    //TODO remove нужен?
    public void removeListener(){
        this.listener = null;
    }

    protected void notifyAboutEvent(GuiElementEvent event){
        listener.callEvent(event);
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

    @SuppressWarnings("unchecked")
    protected MouseClickEventListener getMouseReleaseListener(Consumer<MouseClickEvent<Component>> mouseReleaseAction) {
        return event -> {
            event.getTargetComponent().setFocused(false); //TODO КАКОГО ХУЯ ЭТО В ДВИЖКЕ ??? Хотя бы в сервис
            if (event.getAction() == MouseClickEvent.MouseClickAction.RELEASE) {
                mouseReleaseAction.accept(event);
            }
        };
    }
}
