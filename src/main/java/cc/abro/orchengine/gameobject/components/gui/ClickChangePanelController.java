package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;

public class ClickChangePanelController extends GuiElementController<ClickChangePanelGuiEvent> {

    @Override
    public Class<ClickChangePanelGuiEvent> getProcessedEventClass() {
        return ClickChangePanelGuiEvent.class;
    }

    @Override
    public void processEvent(ClickChangePanelGuiEvent event) {
        EventableGuiPanel guiPanel = event.getNextPanelSupplier().get();
        EventableGuiElement<EventableGuiPanel> guiElement = new EventableGuiElement<>(guiPanel);
        getGuiElement().destroyAndCreateGuiElement(guiElement);
    }
}