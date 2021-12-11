package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.gui.EventableGuiElement;
import cc.abro.orchengine.gameobject.components.gui.GuiElementController;
import cc.abro.orchengine.gameobject.components.gui.GuiElementEvent;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Класс кеширующий контроллеры панелей меню. Позволяет по ивенту получить контроллер для обработки этого ивента.
 */
@Log4j2
public class PanelControllersStorage {

    private final Map<Class<? extends GuiElementEvent>, Supplier<GuiElementController>> controllerByEvent = new HashMap<>();

    public void registry(Supplier<GuiElementController> controllerSupplier) {
        Class<? extends GuiElementEvent> eventClass = controllerSupplier.get().getProcessedEventClass();
        if (controllerByEvent.containsKey(eventClass)) {
            log.error("Event class \"" + eventClass + "\" already exists");
            throw new IllegalStateException("Event class \"" + eventClass + "\" already exists");
        }
        controllerByEvent.put(eventClass, controllerSupplier);
    }

    public void processEvent(GuiElementEvent event, EventableGuiElement eventableGuiElement) {
        GuiElementController controller = getController(event.getClass());
        controller.init(eventableGuiElement);
        controller.processEvent(event);
    }

    private GuiElementController getController(Class<? extends GuiElementEvent> eventClass) {
        if (!controllerByEvent.containsKey(eventClass)) {
            log.error("Event class \"" + eventClass + "\" not found");
            throw new IllegalStateException("Controllers for event \"" + eventClass + "\" not found");
        }

        return controllerByEvent.get(eventClass).get();
    }
}