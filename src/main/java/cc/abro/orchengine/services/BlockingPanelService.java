package cc.abro.orchengine.services;

import cc.abro.orchengine.gui.EventableGuiPanel;
import org.liquidengine.legui.component.Component;

import java.util.Set;
import java.util.stream.Collectors;

public class BlockingPanelService {

    public GuiPanelBlock createGuiPanelBlock(EventableGuiPanel panel) {
        return new GuiPanelBlock(panel);
    }

    public final static class GuiPanelBlock {
        private final Set<Component> unfocusedComponents;

        public GuiPanelBlock(EventableGuiPanel panel) {
            unfocusedComponents = panel.getChildComponents().stream()
                    .filter(Component::isFocusable)
                    .peek(component -> component.setFocusable(false))
                    .collect(Collectors.toUnmodifiableSet());
        }

        public Set<Component> getUnfocusedComponents() {
            return unfocusedComponents;
        }

        public void unblock() {
            getUnfocusedComponents().iterator().forEachRemaining(c -> c.setFocusable(true));
        }
    }
}
