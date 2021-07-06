package cc.abro.orchengine.gui;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс кеширующий готовые панели меню, чтобы сохранять все вносимые изменения в интерфейс
 */
@Log4j2
public class GuiPanelStorage {

    private final Map<String, GuiPanel> guiPanelByName = new HashMap<>();

    public void registry(GuiPanel guiPanel) {
        registry(guiPanel.getClass().getCanonicalName(), guiPanel);
    }

    public void registry(String name, GuiPanel guiPanel) {
        if (guiPanelByName.containsKey(name)) {
            log.error("GuiPanel \"" + name + "\" already exists");
            throw new IllegalStateException("GuiPanel \"" + name + "\" already exists");
        }
        guiPanelByName.put(name, guiPanel);
    }

    public <T> T getPanel(Class<T> guiPanelClass) {
        return getPanel(guiPanelClass.getCanonicalName());
    }

    @SuppressWarnings("unchecked")
    public <T> T getPanel(String name) {
        if (!guiPanelByName.containsKey(name)) {
            log.error("GuiPanel \"" + name + "\" not found");
            return null;
        }

        return (T) guiPanelByName.get(name);
    }
}
