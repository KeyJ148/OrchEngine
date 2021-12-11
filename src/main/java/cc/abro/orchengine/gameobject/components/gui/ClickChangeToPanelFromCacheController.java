package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.gui.EventableGuiPanel;
import cc.abro.orchengine.gui.GuiPanelStorage;

public class ClickChangeToPanelFromCacheController extends GuiElementController<ClickChangeToPanelFromCacheGuiEvent> {

    @Override
    public Class<ClickChangeToPanelFromCacheGuiEvent> getProcessedEventClass() {
        return ClickChangeToPanelFromCacheGuiEvent.class;
    }

    @Override
    public void processEvent(ClickChangeToPanelFromCacheGuiEvent event) {
        EventableGuiPanel guiPanel = Manager.getService(GuiPanelStorage.class).getPanel(event.getNextPanelClass());
        EventableGuiElement<EventableGuiPanel> guiElement = new EventableGuiElement<>(guiPanel);
        getGuiElement().destroyAndCreateGuiElement(guiElement);
    }
}
