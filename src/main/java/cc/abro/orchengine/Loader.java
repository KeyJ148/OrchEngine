package cc.abro.orchengine;

import cc.abro.orchengine.analysis.Analyzer;
import cc.abro.orchengine.audio.AudioPlayer;
import cc.abro.orchengine.cycle.Engine;
import cc.abro.orchengine.cycle.GUI;
import cc.abro.orchengine.cycle.Render;
import cc.abro.orchengine.cycle.Update;
import cc.abro.orchengine.gui.GuiPanelStorage;
import cc.abro.orchengine.gui.PanelControllersStorage;
import cc.abro.orchengine.gui.input.mouse.MouseCursor;
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
import cc.abro.orchengine.profiles.Profile;
import cc.abro.orchengine.profiles.Profiles;
import cc.abro.orchengine.resources.animations.AnimationStorage;
import cc.abro.orchengine.resources.audios.AudioStorage;
import cc.abro.orchengine.resources.settings.SettingsStorageHandler;
import cc.abro.orchengine.resources.sprites.SpriteStorage;
import cc.abro.orchengine.services.GuiElementService;
import cc.abro.orchengine.services.LeguiComponentService;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class Loader {

	public static void start(GameInterface game, NetGameReadInterface netGameRead,
							 ServerInterface server, NetServerReadInterface netServerRead){
		Manager.addService(game);
		Manager.addService(netGameRead);
		Manager.addService(server);
		Manager.addService(netServerRead);
		new Loader().tryInit();
	}

	public static void start(Class<? extends GameInterface> gameClass, Class<? extends NetGameReadInterface> netGameReadClass,
							 Class<? extends ServerInterface> serverClass, Class<? extends NetServerReadInterface> netServerReadClass) {
		Manager.addService(netGameReadClass);
		Manager.addService(gameClass);
		Manager.addService(serverClass);
		Manager.addService(netServerReadClass);
		new Loader().tryInit();
	}

	public static void start(GameInterface game, NetGameReadInterface netGameRead,
							 ServerInterface server, NetServerReadInterface netServerRead, Profile profile){
		Profiles.initProfile(profile);
		start(game, netGameRead, server, netServerRead);
	}

	public static void start(Class<? extends GameInterface> gameClass, Class<? extends NetGameReadInterface> netGameReadClass,
							 Class<? extends ServerInterface> serverClass, Class<? extends NetServerReadInterface> netServerReadClass,
							 Profile profile) {
		Profiles.initProfile(profile);
		start(gameClass, netGameReadClass, serverClass, netServerReadClass);
	}

	public void tryInit(){
		try {
			loggerInit();//Загрузка логгера для вывода ошибок
			init(); //Инициализация перед запуском
			Global.engine.run();//Запуск главного цикла
			stop(); //Освобождение ресурсов
		} catch (Exception e) {
			logException(e);
			stop(e);
		}
	}

	private void loggerInit() {
		try {
			SettingsStorageHandler.init();//Загрузка настроек
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
	}

	//Инициализация движка перед запуском
	private void init() {
		Manager.addService(Engine.class);
		Manager.addService(Update.class);
		Manager.addService(Render.class);
		Manager.addService(GUI.class);
		Manager.addService(Analyzer.class);
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
		Manager.addService(GuiElementService.class);
		Manager.addService(LeguiComponentService.class);

		Manager.addBean(Connector.class);
		Manager.addBean(MouseCursor.class);

		log.info("Initialization start");

		Manager.start();
		Global.engine = Manager.getService(Engine.class);

		log.info("Initialization end");

		new Location(640, 480).activate(false);
		Manager.getService(GameInterface.class).init();
	}

	private void stop(Exception e) {
		stop();
		throw new RuntimeException(e);
	}

	private void stop() {
		try {
			Manager.stop();
			log.debug("Exit stack trace: ", new Exception());
		} catch (Exception e) {
			logException(e);
		}
	}

	private void logException(Exception e){
		e.printStackTrace();
		try {
			log.fatal("Unknown exception: ", e);
		} catch (Exception logException){
			logException.printStackTrace();
		}
	}
}
