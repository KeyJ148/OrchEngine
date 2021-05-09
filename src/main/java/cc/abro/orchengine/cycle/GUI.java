package cc.abro.orchengine.cycle;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Loader;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.animation.AnimatorProvider;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.system.layout.LayoutManager;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;

@Log4j2
public class GUI {

    private DefaultInitializer initializer; //Инициализатор LeGUI

    public GUI() {
        try {
            //Инициализация интерфейса
            initLeGUI();
        } catch (Exception e) {
            log.fatal("LeGUI initialization failed", e);
            Loader.exit();
        }
    }

    private void initLeGUI() {
        Frame frame = createFrame();

        initializer = new DefaultInitializer(Global.engine.render.getWindowID(), frame);
        initializer.getRenderer().initialize();
    }

    public Frame createFrame() {
        Frame frame = new Frame(Global.engine.render.getWidth(), Global.engine.render.getHeight());
        frame.getContainer().setFocusable(true);

        return frame;
    }

    public void render() {
        //Обновление интерфейса в соответствие с параметрами окна
        initializer.getContext().updateGlfwWindow();

        //Отрисовка интерфейса
        initializer.getRenderer().render(Global.location.getGuiFrame(), initializer.getContext());

        //Нормализация параметров OpenGL после отрисовки интерфейса
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void pollEvents() {
        //Получение событий ввода и других callbacks от OpenGL
        glfwPollEvents();

        //Обработка событий (Системных и GUI)
        initializer.getSystemEventProcessor().processEvents(Global.location.getGuiFrame(), initializer.getContext());
        initializer.getGuiEventProcessor().processEvents();

        //Перерасположить компоненты
        LayoutManager.getInstance().layout(Global.location.getGuiFrame());

        //Запуск анимаций
        AnimatorProvider.getAnimator().runAnimations();
    }

    public void setFrameFocused(Frame frame) {
        initializer.getContext().setFocusedGui(frame.getContainer());
    }
}
