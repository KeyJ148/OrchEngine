package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;

import java.util.function.Supplier;

public class ClickChangePanelGuiEvent implements GuiElementEvent {

    private final Supplier<EventableGuiPanel> nextPanelSupplier;

    public ClickChangePanelGuiEvent(Supplier<EventableGuiPanel> nextPanelSupplier) {
        this.nextPanelSupplier = nextPanelSupplier;
    }

    public Supplier<EventableGuiPanel> getNextPanelSupplier() {
        return nextPanelSupplier;
    }
}