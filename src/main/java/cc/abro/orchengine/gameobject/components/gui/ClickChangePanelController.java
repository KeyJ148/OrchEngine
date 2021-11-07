package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;

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