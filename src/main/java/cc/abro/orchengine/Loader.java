package cc.abro.orchengine;

import cc.abro.orchengine.audio.AudioPlayer;
import cc.abro.orchengine.cycle.Engine;
import cc.abro.orchengine.gui.CachedGuiPanelStorage;
import cc.abro.orchengine.implementation.GameInterface;
import cc.abro.orchengine.implementation.NetGameReadInterface;
import cc.abro.orchengine.implementation.NetServerReadInterface;
import cc.abro.orchengine.implementation.ServerInterface;
import cc.abro.orchengine.map.Location;
import cc.abro.orchengine.net.client.Ping;
import cc.abro.orchengine.net.client.tcp.TCPControl;
import cc.abro.orchengine.net.client.tcp.TCPRead;
import cc.abro.orchengine.net.client.udp.UDPControl;
import cc.abro.orchengine.net.client.udp.UDPRead;
import cc.abro.orchengine.resources.animations.AnimationStorage;
import cc.abro.orchengine.resources.audios.AudioStorage;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import cc.abro.orchengine.resources.settings.SettingsStorageHandler;
import cc.abro.orchengine.resources.sprites.SpriteStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;

import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Loader {

	private static final Logger log = LogManager.getLogger(Loader.class);

	public static void start(GameInterface game, NetGameReadInterface netGameRead,
							 ServerInterface server, NetServerReadInterface netServerRead) {
		Global.game = game;
		Global.server = server;
		Global.netGameRead = netGameRead;
		Global.netServerRead = netServerRead;

		try {
			loggerInit();//Загрузка логгера для вывода ошибок
			init(); //Инициализация перед запуском
			Global.engine.run();//Запуск главного цикла
		} catch (Exception e) {
			e.printStackTrace();
			log.fatal("Unknown exception: ", e); //TODO: если logger не создан
			exit();
		}
	}

	private static void loggerInit() {
		try {
			SettingsStorageHandler.init();//Загрузка настроек
		} catch (IOException e) {
			e.printStackTrace();
			exit();
		}

		/* TODO
		Global.logger = new AggregateLogger();

		//Установка настроек логирования
		Global.logger.enableType(Logger.Type.INFO);
		Global.logger.enableType(Logger.Type.SERVER_INFO);
		if (SettingsStorage.LOGGER.ERROR_CONSOLE) Global.logger.enableType(Logger.Type.ERROR);
		if (SettingsStorage.LOGGER.ERROR_CONSOLE_SERVER) Global.logger.enableType(Logger.Type.SERVER_ERROR);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE) Global.logger.enableType(Logger.Type.DEBUG);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_IMAGE) Global.logger.enableType(Logger.Type.DEBUG_TEXTURE);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_MASK) Global.logger.enableType(Logger.Type.DEBUG_MASK);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_AUDIO) Global.logger.enableType(Logger.Type.DEBUG_AUDIO);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_FPS) Global.logger.enableType(Logger.Type.CONSOLE_FPS);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_SERVER) Global.logger.enableType(Logger.Type.SERVER_DEBUG);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_MPS) Global.logger.enableType(Logger.Type.MPS);*/
	}

	//Инициализация движка перед запуском
	private static void init() {
		Global.engine = new Engine();//Создание класса для главного цикла
		Global.engine.init();

		Global.tcpControl = new TCPControl();
		Global.tcpRead = new TCPRead();
		Global.udpControl = new UDPControl();
		Global.udpRead = new UDPRead();

		Global.pingCheck = new Ping();

		Global.audioPlayer = new AudioPlayer();
		Global.audioStorage = new AudioStorage();

		Global.spriteStorage = new SpriteStorage();
		Global.animationStorage = new AnimationStorage();
		Global.cachedGuiPanelStorage = new CachedGuiPanelStorage();

		new Location(640, 480).activate(false);

		log.info("Initialization end");

		//Инициализация игры
		Global.game.init();
	}

	public static void exit() {
		try {
			glfwFreeCallbacks(Global.engine.render.getWindowID());
			glfwDestroyWindow(Global.engine.render.getWindowID());
			glfwTerminate();
			GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
			if (errorCallback != null) errorCallback.free();
			Global.audioPlayer.close();

			log.debug("Exit stack trace: ", new Exception());
		} catch (Exception e) {
			log.error("Unknown exception: ", e); //TODO: если logger не создан
		} finally {
			//Global.logger.close(); TODO
			System.exit(0);
		}
	}


}
