package cc.abro.orchengine.gui;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.services.GuiService;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.listener.MouseClickEventListener;

import java.util.function.Supplier;

public interface MouseReleaseChangePanelListeners extends MouseReleaseListeners{

    Component getRemovableComponentToChangePanel();

    default MouseClickEventListener getChangeToPanelReleaseListener(Supplier<Panel> newPanelSupplier) {
        GuiService guiService = Manager.getService(GuiService.class);
        return getMouseReleaseListener(() -> {
            Panel newPanel = newPanelSupplier.get();
            guiService.moveComponentToWindowCenter(newPanel);
            guiService.replaceComponent(getRemovableComponentToChangePanel(), newPanel);
        });
    }

    default MouseClickEventListener getChangeToCachedPanelReleaseListener(Class<? extends Panel> newPanelClass) {
        return getChangeToPanelReleaseListener(() -> Manager.getService(GuiPanelStorage.class).getPanel(newPanelClass));
    }

    default MouseClickEventListener getChangeToPanelReleaseListener(Panel newPanel) {
        return getChangeToPanelReleaseListener(() -> newPanel);
    }
}
