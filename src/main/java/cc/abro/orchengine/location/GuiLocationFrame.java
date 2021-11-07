package cc.abro.orchengine.location;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gui.input.keyboard.KeyboardHandler;
import cc.abro.orchengine.gui.input.mouse.MouseHandler;
import cc.abro.orchengine.services.GuiService;
import org.liquidengine.legui.component.Frame;

public class GuiLocationFrame {

    private final Frame guiFrame; //Объект хранящий все элементы gui в данной комнате
    protected KeyboardHandler keyboard; //Объект хранящий события клавитуры
    protected MouseHandler mouse; //Объект хранящий события мыши и рисующий курсор на экране

    public GuiLocationFrame() {
        guiFrame = Manager.getService(GuiService.class).createFrame();
    }

    public void pollEvents() {
        Manager.getService(GuiService.class).pollEvents(getGuiFrame());
    }

    public void update() {}

    public void render() {
        Manager.getService(GuiService.class).render(getGuiFrame()); //Отрисовка интерфейса (LeGUI)
        getMouse().draw(); //Отрисовка курсора мыши
    }

    public void destroy() {}

    //Добавление объекта в комнату
    public void objAdd(GameObject gameObject) {}

    //Удаление объекта из комнаты по id
    public void objDel(int id) {}

    public Frame getGuiFrame() {
        return guiFrame;
    }

    public KeyboardHandler getKeyboard() {
        return keyboard;
    }

    public MouseHandler getMouse() {
        return mouse;
    }
}
