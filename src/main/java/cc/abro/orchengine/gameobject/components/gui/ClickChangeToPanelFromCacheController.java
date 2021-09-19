package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.gui.EventableGuiPanel;
import cc.abro.orchengine.gui.GuiPanelStorage;
import cc.abro.orchengine.gui.PanelControllersStorage;

public class ClickChangeToPanelFromCacheController extends GuiElementController<ClickChangeToPanelFromCacheGuiEvent> {

    @Override
    protected Class<ClickChangeToPanelFromCacheGuiEvent> getProcessedEventClass() {
        return ClickChangeToPanelFromCacheGuiEvent.class;
    }

    @Override
    public void processEvent(ClickChangeToPanelFromCacheGuiEvent event) {
        EventableGuiPanel guiPanel = Manager.getService(GuiPanelStorage.class).getPanel(event.getNextPanelClass());
        EventableGuiPanelElement<EventableGuiPanel> guiElement = new EventableGuiPanelElement<>(
                guiPanel, Manager.getService(PanelControllersStorage.class).getControllers(event.getNextPanelClass()));
        getGuiElement().destroyAndCreateGuiElement(guiElement);
    }
}
