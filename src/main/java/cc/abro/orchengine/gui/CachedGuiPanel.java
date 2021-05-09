package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.render.CachedGuiElement;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.ToggleButton;
import cc.abro.orchengine.gameobject.components.render.GuiElement;

/**
 * Абстрактный класс от которого наследуются классы готовых панелей, которые надо кешировать (например, меню).
 * Подробнее {@link GuiElement} и {@link CachedComponent}
 */
public abstract class CachedGuiPanel extends GuiPanel implements CachedComponent<Panel> {

    private CachedGuiElement<Panel> cachedGuiElementOnActiveLocation;

    @Override
    public void setCachedGuiElementOnActiveLocation(CachedGuiElement<Panel> cachedGuiElementOnActiveLocation) {
        this.cachedGuiElementOnActiveLocation = cachedGuiElementOnActiveLocation;
    }

    @Override
    public CachedGuiElement<Panel> getCachedGuiElementOnActiveLocation() {
        return cachedGuiElementOnActiveLocation;
    }

    @Override
    public Panel getComponent() {
        return this;
    }
}
