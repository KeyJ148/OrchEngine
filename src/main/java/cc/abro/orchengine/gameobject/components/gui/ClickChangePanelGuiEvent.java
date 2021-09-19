package cc.abro.orchengine.gameobject.components.gui;

import cc.abro.orchengine.gui.EventableGuiPanel;

import java.util.Set;
import java.util.function.Supplier;

public class ClickChangePanelGuiEvent implements GuiElementEvent {

    private final Supplier<EventableGuiPanel> nextPanelSupplier;
    private final Supplier<Set<GuiElementController>> controllersSupplier;

    public ClickChangePanelGuiEvent(Supplier<EventableGuiPanel> nextPanelSupplier,
                                    Supplier<Set<GuiElementController>> controllersSupplier) {
        this.nextPanelSupplier = nextPanelSupplier;
        this.controllersSupplier = controllersSupplier;
    }

    public Supplier<EventableGuiPanel> getNextPanelSupplier() {
        return nextPanelSupplier;
    }

    public Supplier<Set<GuiElementController>> getControllersSupplier() {
        return controllersSupplier;
    }
}