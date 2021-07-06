package cc.abro.orchengine;

import cc.abro.orchengine.cycle.Engine;
import cc.abro.orchengine.map.Location;

public class Global {

	public static Location location; //Текущая комната
	public static Engine engine; //Главный игровой поток

	//public static AudioPlayer audioPlayer; //Объект, воспроизводящий музыку и хранящий источники музыки

	//public static AudioStorage audioStorage; //Объект хранящий звуки (буфферы OpenAL)
	//public static SpriteStorage spriteStorage; //Объект хранящий спрайты
	//public static AnimationStorage animationStorage; //Объект хранящий анимации
	//public static GuiPanelStorage guiPanelStorage;
	//public static PanelControllersStorage panelControllersStorage;

	//TODO: убрать в главный класс Network при рефакторинге сети
	//public static TCPControl tcpControl; //Хранит настройки и работает с сетью по TCP протоколу
	//public static TCPRead tcpRead; //Цикл считывания данных с сервера по TCP протоколу
	//public static UDPControl udpControl; //Хранит настройки и работает с сетью по UDP протоколу
	//public static UDPRead udpRead; //Цикл считывания данных с сервера по UDP протоколу
	//public static PingChecker pingCheck;//Объект для проверки пинга

	//TODO Объекты реализуемые вне движка и передаваемые при старте
	//public static GameInterface game; //Главный объект игры
	//public static ServerInterface server; //Главный объект сервера
	//public static NetGameReadInterface netGameRead; //Объект для обработки сетевых сообщений на клиенте
	//public static NetServerReadInterface netServerRead; //Объект для обработки сетевых сообщений на сервере
}

