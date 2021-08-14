package cc.abro.orchengine.analysis;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.net.client.PingChecker;
import cc.abro.orchengine.net.client.tcp.TCPControl;
import cc.abro.orchengine.net.client.udp.UDPControl;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import lombok.extern.log4j.Log4j2;
import org.picocontainer.Startable;

@Log4j2
public class Analyzer implements Startable {

	//Для подсчёта fps, ups
	public int loopsRender = 0;
	public int loopsUpdate = 0;
	public int loopsSync = 0;
	private long startUpdate, startRender, startSync, lastAnalysis;

	//Для подсчёта быстродействия
	public long durationUpdate = 0;
	public long durationRender = 0;
	public long durationSync = 0;

	//Пинг
	public int ping = 0, pingMin = 0, pingMax = 0, pingMid = 0;

	//Скорость сети
	public int sendTCP = 0, loadTCP = 0, sendPackageTCP = 0, loadPackageTCP = 0;
	public int sendUDP = 0, loadUDP = 0, sendPackageUDP = 0, loadPackageUDP = 0;

	//Использование памяти
	public long freeMem = 0, totalMem = 0, maxMem = 0;

	public int chunkInDepthVector;

	private AnalysisStringBuilder analysisStringBuilder;

	private TCPControl tcpControl;
	private UDPControl udpControl;
	private PingChecker pingCheckerCheck;

	public Analyzer(TCPControl tcpControl, UDPControl udpControl, PingChecker pingCheckerCheck) {
		this.tcpControl = tcpControl;
		this.udpControl = udpControl;
		this.pingCheckerCheck = pingCheckerCheck;
	}

	@Override
	public void start() {
		analysisStringBuilder = new AnalysisStringBuilder(this);
		lastAnalysis = System.currentTimeMillis();
	}

	@Override
	public void stop() { }

	public void startUpdate() {
		startUpdate = System.nanoTime();
	}

	public void endUpdate() {
		durationUpdate += System.nanoTime() - startUpdate;
		loopsUpdate++;
	}

	public void startRender() {
		startRender = System.nanoTime();
	}

	public void endRender() {
		durationRender += System.nanoTime() - startRender;
		loopsRender++;
	}

	public void startSync() {
		startSync = System.nanoTime();
	}

	public void endSync() {
		durationSync += System.nanoTime() - startSync;
		loopsSync++;
	}

	public void update() {
		//Если прошло меньше секунды - выходим
		if (System.currentTimeMillis() < lastAnalysis + 1000) return;

		analysisData();
		outputResult();
		clearData();
	}

	//Анализ данных
	public void analysisData() {
		ping = pingCheckerCheck.ping();
		pingMin = pingCheckerCheck.pingMin();
		pingMid = pingCheckerCheck.pingMid();
		pingMax = pingCheckerCheck.pingMax();

		sendTCP = Math.round(tcpControl.sizeDataSend / 1024);
		loadTCP = Math.round(tcpControl.sizeDataRead / 1024);
		sendPackageTCP = tcpControl.countPackageSend;
		loadPackageTCP = tcpControl.countPackageRead;
		tcpControl.analyzeClear();

		sendUDP = Math.round(udpControl.sizeDataSend / 1024);
		loadUDP = Math.round(udpControl.sizeDataRead / 1024);
		sendPackageUDP =udpControl.countPackageSend;
		loadPackageUDP =udpControl.countPackageRead;
		udpControl.analyzeClear();

		freeMem = Runtime.getRuntime().freeMemory();
		totalMem = Runtime.getRuntime().totalMemory();
		maxMem = Runtime.getRuntime().maxMemory();

		chunkInDepthVector = (Global.location.mapControl.getCountDepthVectors() == 0) ? 0 : Global.location.mapControl.chunkRender / Global.location.mapControl.getCountDepthVectors();

		//Для строк отладки, иначе делние на 0
		if (loopsRender == 0) loopsRender = 1;
		if (loopsUpdate == 0) loopsUpdate = 1;
		if (loopsSync == 0) loopsSync = 1;
	}

	//Обнуление счётчиков
	public void clearData() {
		lastAnalysis = System.currentTimeMillis();
		loopsUpdate = 0;
		loopsRender = 0;
		durationUpdate = 0;
		durationRender = 0;
	}

	//Вывод результатов
	public void outputResult() {
		//Получение строк с результатами
		String str1 = analysisStringBuilder.getAnalysisString1();
		String str2 = analysisStringBuilder.getAnalysisString2();

		//Вывод результатов в консоль
		log.trace(str1);
		log.trace(str2);

		//Вывод результатов на монитор
		if (SettingsStorage.LOGGER.DEBUG_MONITOR_FPS) {
			//Отрисвока надписей
			/*
			addTitle(new Title(1, getHeight()-27,strAnalysis1, Color.black, 12, Font.BOLD));
			addTitle(new Title(1, getHeight()-15,strAnalysis2, Color.black, 12, Font.BOLD));
			*/
		}
	}
}
