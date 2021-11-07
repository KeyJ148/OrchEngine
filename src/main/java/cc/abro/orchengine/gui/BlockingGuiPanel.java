package cc.abro.orchengine.gui;

import org.liquidengine.legui.component.Component;

import java.util.Set;
import java.util.stream.Collectors;

public class BlockingGuiPanel extends EventableGuiPanel {
    private final Set<Component> unfocusedComponents;

    public BlockingGuiPanel(int sizeX, int sizeY, Component parent) {
        setSize(sizeX, sizeY);

        unfocusedComponents = parent.getChildComponents().stream()
                .filter(Component::isFocusable)
                .peek(component -> component.setFocusable(false))
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Component> getUnfocusedComponents() {
        return unfocusedComponents;
    }

    public void focusComponents() {
        getUnfocusedComponents().iterator().forEachRemaining(c -> c.setFocusable(true));
    }
}
