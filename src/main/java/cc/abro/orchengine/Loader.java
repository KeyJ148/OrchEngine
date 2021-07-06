package cc.abro.orchengine;

import cc.abro.orchengine.analysis.Analyzer;
import cc.abro.orchengine.audio.AudioPlayer;
import cc.abro.orchengine.cycle.Engine;
import cc.abro.orchengine.gui.GuiPanelStorage;
import cc.abro.orchengine.gui.PanelControllersStorage;
import cc.abro.orchengine.cycle.GUI;
import cc.abro.orchengine.cycle.Render;
import cc.abro.orchengine.cycle.Update;
import cc.abro.orchengine.implementation.GameInterface;
import cc.abro.orchengine.implementation.NetGameReadInterface;
import cc.abro.orchengine.implementation.NetServerReadInterface;
import cc.abro.orchengine.implementation.ServerInterface;
import cc.abro.orchengine.map.Location;
import cc.abro.orchengine.net.client.Connector;
import cc.abro.orchengine.net.client.PingChecker;
import cc.abro.orchengine.net.client.tcp.TCPControl;
import cc.abro.orchengine.net.client.tcp.TCPRead;
import cc.abro.orchengine.net.client.udp.UDPControl;
import cc.abro.orchengine.net.client.udp.UDPRead;
import cc.abro.orchengine.resources.animations.AnimationStorage;
import cc.abro.orchengine.resources.audios.AudioStorage;
import cc.abro.orchengine.resources.settings.SettingsStorageHandler;
import cc.abro.orchengine.resources.sprites.SpriteStorage;
import lombok.extern.log4j.Log4j2;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

@Log4j2
public class Loader {

	public static void start(GameInterface game, NetGameReadInterface netGameRead,
							 ServerInterface server, NetServerReadInterface netServerRead){
		Manager.addService(game);
		Manager.addService(netGameRead);
		Manager.addService(server);
		Manager.addService(netServerRead);
		tryInit();
	}

	public static void start(Class<? extends GameInterface> gameClass, Class<? extends NetGameReadInterface> netGameReadClass,
							 Class<? extends ServerInterface> serverClass, Class<? extends NetServerReadInterface> netServerReadClass) {
		Manager.addService(netGameReadClass);
		Manager.addService(gameClass);
		Manager.addService(serverClass);
		Manager.addService(netServerReadClass);
		tryInit();
	}

	private static void tryInit(){
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

		log.info("Initialization start");

		Manager.addService(Engine.class);
		Manager.addService(Update.class);//
		Manager.addService(Render.class);//
		Manager.addService(GUI.class);//
		Manager.addService(Analyzer.class);//
		Manager.addService(TCPControl.class);
		Manager.addService(TCPRead.class);
		Manager.addService(UDPControl.class);
		Manager.addService(UDPRead.class);
		Manager.addService(PingChecker.class);
		Manager.addService(AudioPlayer.class);
		Manager.addService(AudioStorage.class);
		Manager.addService(SpriteStorage.class);
		Manager.addService(AnimationStorage.class);
		Manager.addService(GuiPanelStorage.class);
		Manager.addService(PanelControllersStorage.class);

		Manager.addBean(Connector.class);

		Manager.start();

		log.info("Initialization start");

		Global.engine = Manager.getService(Engine.class);
		//Manager.getService(AudioPlayer.class) = Manager.getService(AudioPlayer.class);

		//Manager.getService(TCPControl.class) = new TCPControl();
		//Global.tcpRead = Manager.getService(TCPRead.class);
		//Global.udpControl = new UDPControl();
		//Global.udpRead = Manager.getService(UDPRead.class);
		//Global.pingCheck = new PingChecker();
		//Manager.getService(AudioStorage.class) = new AudioStorage();
		//Manager.getService(SpriteStorage.class) = new SpriteStorage();
		//Manager.getService(AnimationStorage.class) = new AnimationStorage();
		//Global.guiPanelStorage = new GuiPanelStorage();
		//Global.panelControllersStorage = new PanelControllersStorage();

		//TODO game.class -- getGameService
		//Global.game = Manager.getService(GameInterface.class);//TODO мы должны иметь возможность внедрить зависимости в клиенте/игре в любой класс,
		//Global.netGameRead = Manager.getService(NetGameReadInterface.class);//TODO а не только в те 4, что передаем при старте. Должен быть доступ к picoContainer
		//Global.server = Manager.getService(ServerInterface.class);//TODO
		//Global.netServerRead = Manager.getService(NetServerReadInterface.class);//TODO

		new Location(640, 480).activate(false);
		log.info("Initialization end");

		Manager.getService(GameInterface.class).init();
	}

	public static void exit() {
		try {
			glfwFreeCallbacks(Global.engine.render.getWindowID());
			glfwDestroyWindow(Global.engine.render.getWindowID());
			glfwTerminate();
			GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
			if (errorCallback != null) errorCallback.free();
			Manager.stop();

			log.debug("Exit stack trace: ", new Exception());
		} catch (Exception e) {
			log.error("Unknown exception: ", e); //TODO: если logger не создан
		} finally {
			//Global.logger.close(); TODO
			System.exit(0);
		}
	}


}
