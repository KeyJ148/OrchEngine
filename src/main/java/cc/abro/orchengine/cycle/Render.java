package cc.abro.orchengine.cycle;

import cc.abro.orchengine.EngineException;
import cc.abro.orchengine.implementation.GameInterface;
import cc.abro.orchengine.location.LocationManager;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import lombok.extern.log4j.Log4j2;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.component.Frame;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.picocontainer.Startable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Log4j2
public class Render implements Startable {

	private final GameInterface game;
	private final LocationManager locationManager;

	private long windowID; //ID окна игры для LWJGL
	private long monitorID; //ID монитора (0 для не полноэкранного режима)
	private DefaultInitializer leguiInitializer; //Инициализатор LeGUI
	private int width;
	private int height;

	public Render(GameInterface game, LocationManager locationManager) {
		this.game = game;
		this.locationManager = locationManager;
	}

	@Override
	public void start() {
		//Инициализация GLFW
		if (!glfwInit()) {
			log.fatal("GLFW initialization failed");
			throw new EngineException("GLFW initialization failed");
		}

		//Инициализация и настройка окна
		try {
			initOpenGL();
		} catch (Exception e) {
			log.fatal("OpenGL initialization failed", e);
			throw e;
		}

		//Инициализация и настройка LeGUI
		try {
			leguiInitializer = new DefaultInitializer(getWindowID(), new Frame());
			leguiInitializer.getRenderer().initialize();
		} catch (Exception e) {
			log.fatal("LeGUI initialization failed", e);
			throw e;
		}
	}

	private void initOpenGL() {
		//Установка параметров для окна
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		//Получение разрешения экрана
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int monitorWidth = vidMode.width();
		int monitorHeight = vidMode.height();

		//Выбор экрана и размеров окна
		if (SettingsStorage.DISPLAY.FULL_SCREEN) {
			monitorID = glfwGetPrimaryMonitor();
			width = monitorWidth;
			height = monitorHeight;
		} else {
			monitorID = 0;
			width = SettingsStorage.DISPLAY.WIDTH_SCREEN;
			height = SettingsStorage.DISPLAY.HEIGHT_SCREEN;
		}

		//Создание окна
		windowID = glfwCreateWindow(width, height, SettingsStorage.DISPLAY.WINDOW_NAME, monitorID, NULL);
		//Перемещение окна на центр монитора
		glfwSetWindowPos(windowID, (monitorWidth - width) / 2, (monitorHeight - height) / 2);
		//Создание контекста GLFW
		glfwMakeContextCurrent(windowID);
		//Включение VSync: будет происходить синхронизация через каждые N кадров
		glfwSwapInterval(SettingsStorage.DISPLAY.VSYNC_DIVIDER);
		//Создание контекста OpenGL
		GL.createCapabilities();

		//Настройка графики
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		//Включение видимости окна
		glfwShowWindow(windowID);
		//Заливка всего фона черным цветом
		GL11.glClearColor(0f, 0f, 0f, 0f);
		//Обновление кадра
		glfwSwapBuffers(windowID);
	}


	public void loop() {
		glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT); //Очистка рендера

		game.render(); //Отрисовка в главном игровом классе (ссылка передается в движок при инициализации)
		locationManager.getActiveLocation().render(getWidth(), getHeight()); //Отрисовка комнаты
	}

	@Override
	public void stop() {
		glfwFreeCallbacks(getWindowID());
		glfwDestroyWindow(getWindowID());
		glfwTerminate();
		GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
		if (errorCallback != null) errorCallback.free();
	}

	public void vsync() {
		glfwSwapBuffers(windowID);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public long getWindowID() {
		return windowID;
	}

	public long getMonitorID() {
		return monitorID;
	}

	public DefaultInitializer getLeguiInitializer() {
		return leguiInitializer;
	}
}
