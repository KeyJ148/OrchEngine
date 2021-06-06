package cc.abro.orchengine.gameobject.components.gui;

@FunctionalInterface
public interface GuiElementEvent {

    Class<? extends GuiElementController> getControllerClass();
}