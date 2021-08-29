package cc.abro.orchengine.cycle;

import cc.abro.orchengine.EngineException;
import cc.abro.orchengine.Global;
import cc.abro.orchengine.analysis.Analyzer;
import cc.abro.orchengine.implementation.GameInterface;
import cc.abro.orchengine.net.client.tcp.TCPRead;
import cc.abro.orchengine.net.client.udp.UDPRead;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Update {

	private long startUpdateTime, lastUpdateTime = 0;//Для вычисления delta

	private final GameInterface game;
	private final TCPRead tcpRead;
	private final UDPRead udpRead;
	private final GUI gui;
	private final Analyzer analyzer;

	public Update(GameInterface game, TCPRead tcpRead, UDPRead udpRead, GUI gui, Analyzer analyzer) {
		this.game = game;
		this.tcpRead = tcpRead;
		this.udpRead = udpRead;
		this.gui = gui;
		this.analyzer = analyzer;
	}

	public void loop() {
		//При первом вызове устанавливаем текущее время
		if (lastUpdateTime == 0) lastUpdateTime = System.nanoTime();

		startUpdateTime = System.nanoTime();
		loop(System.nanoTime() - lastUpdateTime);
		lastUpdateTime = startUpdateTime;//Начало предыдущего update, чтобы длительность update тоже учитывалась
	}

	//Обновляем игру в соответствие с временем прошедшим с последнего обновления
	private void loop(long delta) {
		gui.pollEvents();//Получение событий и Callbacks

		game.update(delta);//Обновить главный игровой класс при необходимости

		tcpRead.update();//Обработать все полученные сообщения по TCP
		udpRead.update();//Обработать все полученные сообщения по UDP

		if (Global.location != null) {
			Global.location.update(delta);//Обновить все объекты в комнате
		} else {
			log.fatal("No create location! (Global.location)");
			throw new EngineException("No create location! (Global.location)");
		}

		Global.location.getMouse().update(); //Очистка истории событий мыши
		Global.location.getKeyboard().update(); //Очистка истории событий клавиатуры

		analyzer.update(); //Обновление состояния анализатора
	}

}
