package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;

public class ClickChangePanelGuiEvent implements GuiElementEvent {

    private final Class<? extends EventableGuiPanel> nextPanelClass;

    public ClickChangePanelGuiEvent(Class<? extends EventableGuiPanel> nextPanelClass) {
        this.nextPanelClass = nextPanelClass;
    }

    public Class<? extends EventableGuiPanel> getNextPanelClass() {
        return nextPanelClass;
    }
}
