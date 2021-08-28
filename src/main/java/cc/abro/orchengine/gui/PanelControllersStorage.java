package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.gui.GuiElementController;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Класс кеширующий контроллеры закешированных панелей меню. Используется при переключении между
 * закешированными панелями меню.
 */
@Log4j2
public class PanelControllersStorage {

    private final Map<Class<? extends EventableGuiPanel>, Supplier<Set<GuiElementController>>> controllersByPanel = new HashMap<>();

    public void registry(Class<? extends EventableGuiPanel> panelClass,
                         Supplier<Set<GuiElementController>> controllersSupplier) {
        if (controllersByPanel.containsKey(panelClass)) {
            log.error("Panel class \"" + panelClass + "\" already exists");
            throw new IllegalStateException("Panel class \"" + panelClass + "\" already exists");
        }
        controllersByPanel.put(panelClass, controllersSupplier);
    }

    public Set<GuiElementController> getControllers(Class<? extends EventableGuiPanel> panelClass) {
        if (!controllersByPanel.containsKey(panelClass)) {
            log.error("Panel class \"" + panelClass + "\" not found");
            throw new IllegalStateException("Controllers for panel \"" + panelClass + "\" not found");
        }

        return controllersByPanel.get(panelClass).get();
    }
}