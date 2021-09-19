package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;

public class ClickChangeToPanelFromCacheGuiEvent implements GuiElementEvent {

    private final Class<? extends EventableGuiPanel> nextPanelClass;

    public ClickChangeToPanelFromCacheGuiEvent(Class<? extends EventableGuiPanel> nextPanelClass) {
        this.nextPanelClass = nextPanelClass;
    }

    public Class<? extends EventableGuiPanel> getNextPanelClass() {
        return nextPanelClass;
    }
}
