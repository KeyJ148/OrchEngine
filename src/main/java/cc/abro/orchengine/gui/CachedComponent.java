package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.render.CachedGuiElement;
import cc.abro.orchengine.gameobject.components.render.GuiElement;
import org.liquidengine.legui.component.Component;

/**
 * Это обертка для {@link Component}, который может быть закеширован в {@link CachedGuiPanelStorage}.
 * Подробнее в {@link CachedGuiElement}
 *
 * @param <T> тип компонента, для которого выполнена обертка
 */
public interface CachedComponent<T extends Component> {

    /**
     * Обновить компонент игрового объекта при изменение активной локации.
     *
     * @param cachedGuiElementOnActiveLocation компонент, к которому будет привязан объект реализующий этот интерфейс
     */
    void setCachedGuiElementOnActiveLocation(CachedGuiElement<T> cachedGuiElementOnActiveLocation);

    /**
     * Ссылка на компонент игрового объекта - {@link GuiElement}, который находится на текущей активной локации.
     * Ссылка автоматически обновляется при переносе на другую активную локацию.
     * Подробнее {@link GuiElement}.
     */
    CachedGuiElement<T> getCachedGuiElementOnActiveLocation();

    T getComponent();
}
