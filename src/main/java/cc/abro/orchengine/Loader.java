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
import org.apache.logging.log4j.LogManager;

@Log4j2
public class Loader {

	void tryInit(){
		try {
			Thread.currentThread().setName("Engine");
			registryShutdownCallback(); //Регистрация скриптов для корректного освобождения ресурсов при завершение программы
			SettingsStorageHandler.init(); //Загрузка настроек, в том числе для логгера
			initServicesList(); //Инициализация списка сервисов перед запуском
			initBeansList(); //Инициализация списка бинов перед запуском
			initServices(); //Запуск всех сервисов
			initGame();//Вызов инициализации у класса игры
			Global.engine.run();//Запуск главного цикла движка
		} catch (Exception e) {
			logException("The game ended with an error: ", e);
			throw new RuntimeException(e);
		} finally {
			if (Profiles.getActiveProfile() != Profile.TESTS){
				System.exit(0); //Завершение программы, будет вызван callback для освобождения ресурсов
			} else {
				stop(); //Если это автотесты, то не завершаем приложение, а просто высвобождаем ресурсы и возвращаем управление обратно
			}
		}
	}

	private void registryShutdownCallback() {
		Thread shutdownCallbackThread = new Thread(this::stop);
		shutdownCallbackThread.setName("ShutdownHook");
		Runtime.getRuntime().addShutdownHook(shutdownCallbackThread);
	}

	private void initServicesList() {
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
	}

	private void initBeansList() {
		Manager.addBean(Connector.class);
		Manager.addBean(MouseCursor.class);
	}

	private void initServices() {
		log.info("Initialize engine...");
		Manager.start();
		Global.engine = Manager.getService(Engine.class);
		log.info("Initialize engine complete");
	}

	private void initGame() {
		log.info("Initialize game...");
		new Location(640, 480).activate(false);
		Manager.getService(GameInterface.class).init();
		log.info("Initialize game complete");
	}

	private void logException(String text, Exception e){
		e.printStackTrace();
		try {
			log.fatal(text, e);
		} catch (Exception logException){
			logException.printStackTrace();
		}
	}

	private void stop() {
		try {
			log.debug("Shutting down all services...");
			Manager.stop();
			log.debug("Shutting down all services complete");
		} catch (Exception e) {
			logException("Stopping services ended with an error: ", e);
		}
		log.debug("Shutting down logger");
		LogManager.shutdown();
	}
}
