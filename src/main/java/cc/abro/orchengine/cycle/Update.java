package cc.abro.orchengine.cycle;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Loader;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Log4j2
public class Update {

	private long startUpdateTime, lastUpdateTime = 0;//Для вычисления delta

	public void loop() {
		//При первом вызове устанавливаем текущее время
		if (lastUpdateTime == 0) lastUpdateTime = System.nanoTime();

		startUpdateTime = System.nanoTime();
		loop(System.nanoTime() - lastUpdateTime);
		lastUpdateTime = startUpdateTime;//Начало предыдущего update, чтобы длительность update тоже учитывалась
	}

	//Обновляем игру в соответствие с временем прошедшим с последнего обновления
	private void loop(long delta) {
		Global.engine.gui.pollEvents();//Получение событий и Callbacks

		Global.game.update(delta);//Обновить главный игровой класс при необходимости

		Global.tcpRead.update();//Обработать все полученные сообщения по TCP
		Global.udpRead.update();//Обработать все полученные сообщения по UDP

		if (Global.location != null) {
			Global.location.update(delta);//Обновить все объекты в комнате
		} else {
			log.fatal("No create room! (Global.room)");
			Loader.exit();
		}

		Global.location.getMouse().update(); //Очистка истории событий мыши
		Global.location.getKeyboard().update(); //Очистка истории событий клавиатуры

		Global.engine.analyzer.update(); //Обновление состояния анализатора
	}

}
