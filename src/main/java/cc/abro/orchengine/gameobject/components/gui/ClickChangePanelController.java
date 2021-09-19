package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.gui.EventableGuiPanel;
import cc.abro.orchengine.gui.GuiPanelStorage;
import cc.abro.orchengine.gui.PanelControllersStorage;

public class ClickChangePanelController extends GuiElementController<ClickChangePanelGuiEvent> {

    @Override
    protected Class<ClickChangePanelGuiEvent> getProcessedEventClass() {
        return ClickChangePanelGuiEvent.class;
    }

    @Override
    public void processEvent(ClickChangePanelGuiEvent event) {
        EventableGuiPanel guiPanel = event.getNextPanelSupplier().get();
        EventableGuiPanelElement<EventableGuiPanel> guiElement = new EventableGuiPanelElement<>(
                guiPanel, event.getControllersSupplier().get());
        getGuiElement().destroyAndCreateGuiElement(guiElement);
    }
}