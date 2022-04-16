package cc.abro.orchengine.analysis;

import cc.abro.orchengine.context.Context;
import cc.abro.orchengine.context.EngineService;
import cc.abro.orchengine.location.LocationManager;
import cc.abro.orchengine.location.map.MapControl;
import cc.abro.orchengine.net.client.PingChecker;
import cc.abro.orchengine.net.client.tcp.TCPControl;
import cc.abro.orchengine.net.client.udp.UDPControl;
import lombok.extern.log4j.Log4j2;
import org.picocontainer.Startable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@EngineService
public class Analyzer implements Startable {

	//Для подсчёта fps, ups
	protected int loopsRender = 0;
	protected int loopsUpdate = 0;
	protected int loopsSync = 0;
	private long startUpdate, startRender, startSync, lastAnalysis;

	//Для подсчёта быстродействия
	protected long durationUpdate = 0;
	protected long durationRender = 0;
	protected long durationSync = 0;

	//Пинг
	protected int ping = 0, pingMin = 0, pingMax = 0, pingMid = 0;

	//Скорость сети
	protected int sendTCP = 0, loadTCP = 0, sendPackageTCP = 0, loadPackageTCP = 0;
	protected int sendUDP = 0, loadUDP = 0, sendPackageUDP = 0, loadPackageUDP = 0;

	//Использование памяти
	protected long freeMem = 0, totalMem = 0, maxMem = 0;

	//Использование чанков
	protected int chunkInDepthVector;

	//Результаты анализа построчно
	private List<String> analysisResultStrings = Collections.emptyList();

	private AnalysisStringBuilder analysisStringBuilder;
	private final TCPControl tcpControl;
	private final UDPControl udpControl;
	private final PingChecker pingCheckerCheck;

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
		generateResult();
		logToConsole();
		clearData();
	}

	public List<String> getAnalysisResult() {
		return new ArrayList<>(analysisResultStrings);
	}

	//Анализ данных
	private void analysisData() {
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

		MapControl activeMapControl = Context.getService(LocationManager.class).getActiveLocation().getMap().mapControl;
		chunkInDepthVector = (activeMapControl.getCountDepthVectors() != 0) ?
				activeMapControl.chunkRender / activeMapControl.getCountDepthVectors() : 0; //TODO не актуально, кол-во чанков не равно между DepthVector-ами

		//Для строк отладки, иначе деление на 0
		if (loopsRender == 0) loopsRender = 1;
		if (loopsUpdate == 0) loopsUpdate = 1;
		if (loopsSync == 0) loopsSync = 1;
	}

	//Обнуление счётчиков
	private void clearData() {
		lastAnalysis = System.currentTimeMillis();
		loopsUpdate = 0;
		loopsRender = 0;
		durationUpdate = 0;
		durationRender = 0;
	}

	//Сохранение результатов
	private void generateResult() {
		//Получение строк с результатами
		analysisResultStrings = List.of(analysisStringBuilder.getAnalysisString1(),
				analysisStringBuilder.getAnalysisString2());
	}

	private void logToConsole() {
		analysisResultStrings.forEach(log::trace);
	}
}
