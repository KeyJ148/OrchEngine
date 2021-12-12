package cc.abro.orchengine.services;

import org.liquidengine.legui.component.Component;

import java.util.HashSet;
import java.util.Set;

public class BlockingGuiService {

    public GuiBlock createGuiBlock(Component component) {
        return new GuiBlock(component);
    }

    public final static class GuiBlock {
        private final Set<Component> unfocusedComponents = new HashSet<>();

        public GuiBlock(Component blockableComponent) {
            blockAndAddToSet(blockableComponent);
        }

        public Set<Component> getUnfocusedComponents() {
            return unfocusedComponents;
        }

        public void unblock() {
            getUnfocusedComponents().iterator().forEachRemaining(c -> c.setFocusable(true));
        }

        private void blockAndAddToSet(Component blockableComponent) {
            if (blockableComponent.isFocusable()){
                unfocusedComponents.add(blockableComponent);
                blockableComponent.setFocusable(false);
            }

            for (Component childComponent : blockableComponent.getChildComponents()) {
                blockAndAddToSet(childComponent);
            }
        }
    }
}
