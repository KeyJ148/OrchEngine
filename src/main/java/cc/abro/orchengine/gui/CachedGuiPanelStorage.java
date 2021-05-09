package cc.abro.orchengine.gui;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Loader;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс кеширующий готовые панели меню, чтобы сохранять все вносимые изменения в интерфейс
 */
@Log4j2
public class CachedGuiPanelStorage {

    private final Map<String, CachedGuiPanel> cachedGuiPanelByName = new HashMap<>();

    public void registry(CachedGuiPanel cachedGuiPanel) {
        registry(cachedGuiPanel.getClass().getCanonicalName(), cachedGuiPanel);
    }

    public void registry(String name, CachedGuiPanel cachedGuiPanel) {
        if (cachedGuiPanelByName.containsKey(name)) {
            log.error("CachedGuiPanel \"" + name + "\" already exists");
            Loader.exit();
        }
        cachedGuiPanelByName.put(name, cachedGuiPanel);
    }

    public <T> T getPanel(Class<T> cachedGuiPanelClass) {
        return getPanel(cachedGuiPanelClass.getCanonicalName());
    }

    @SuppressWarnings("unchecked")
    public <T> T getPanel(String name) {
        if (!cachedGuiPanelByName.containsKey(name)) {
            log.error("CachedGuiPanel \"" + name + "\" not found");
            return null;
        }

        return (T) cachedGuiPanelByName.get(name);
    }
}
