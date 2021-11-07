package cc.abro.orchengine.cycle;

import cc.abro.orchengine.EngineException;
import cc.abro.orchengine.analysis.Analyzer;
import cc.abro.orchengine.implementation.GameInterface;
import cc.abro.orchengine.map.LocationManager;
import cc.abro.orchengine.net.client.tcp.TCPRead;
import cc.abro.orchengine.net.client.udp.UDPRead;
import cc.abro.orchengine.services.GuiService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Update {

	private final GameInterface game;
	private final TCPRead tcpRead;
	private final UDPRead udpRead;
	private final GuiService guiService;
	private final Analyzer analyzer;
	private final LocationManager locationManager;

	private long lastUpdateTime;

	public Update(GameInterface game, TCPRead tcpRead, UDPRead udpRead, GuiService guiService, Analyzer analyzer,
				  LocationManager locationManager) {
		this.game = game;
		this.tcpRead = tcpRead;
		this.udpRead = udpRead;
		this.guiService = guiService;
		this.analyzer = analyzer;
		this.locationManager = locationManager;
	}

	public void loop() {
		//При первом вызове устанавливаем текущее время
		if (lastUpdateTime == 0) lastUpdateTime = System.nanoTime();

		long startUpdateTime = System.nanoTime();
		loop(System.nanoTime() - lastUpdateTime);
		lastUpdateTime = startUpdateTime;//Начало предыдущего update, чтобы длительность update тоже учитывалась
	}

	//Обновляем игру в соответствие с временем прошедшим с последнего обновления
	private void loop(long delta) {
		locationManager.getActiveLocation().pollEvents(); //Получение событий и Callbacks

		game.update(delta); //Обновить главный игровой класс при необходимости

		tcpRead.update(); //Обработать все полученные сообщения по TCP
		udpRead.update(); //Обработать все полученные сообщения по UDP

		if (locationManager.getActiveLocation() != null) {
			locationManager.getActiveLocation().update(delta);//Обновить все объекты в комнате
		} else {
			log.fatal("Location did not created!");
			throw new EngineException("Location did not created!");
		}
		locationManager.getUpdatedLocations().forEach(location -> location.update(delta));

		locationManager.getActiveLocation().getMouse().update(); //Очистка истории событий мыши
		locationManager.getActiveLocation().getKeyboard().update(); //Очистка истории событий клавиатуры

		analyzer.update(); //Обновление состояния анализатора
	}

}
