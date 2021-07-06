package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.gui.EventableGuiPanelElement;
import cc.abro.orchengine.gameobject.components.gui.GuiElementController;
import cc.abro.orchengine.gui.EventableGuiPanel;

public class ClickChangePanelController extends GuiElementController<ClickChangePanelGuiEvent> {

    @Override
    protected Class<ClickChangePanelGuiEvent> getProcessedEventClass() {
        return ClickChangePanelGuiEvent.class;
    }

    @Override
    public void processEvent(ClickChangePanelGuiEvent event) {
        EventableGuiPanel guiPanel = Global.guiPanelStorage.getPanel(event.getNextPanelClass());
        EventableGuiPanelElement<EventableGuiPanel> guiElement = new EventableGuiPanelElement<>(
                guiPanel, Global.panelControllersStorage.getControllers(event.getNextPanelClass()));
        getGuiElement().destroyAndCreateGuiElement(guiElement);
    }
}
