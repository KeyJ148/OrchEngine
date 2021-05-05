package cc.abro.orchengine.net.server;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

public class AnalyzerThread extends Thread {

    private static final Logger log = LogManager.getLogger(AnalyzerThread.class);

    @Override
    public void run() {
        long timeAnalysis = System.currentTimeMillis();//Время анализа MPS
        int timeAnalysisDelta = 1000;

        while (true) {
            if (System.currentTimeMillis() > timeAnalysis + timeAnalysisDelta) {//Анализ MPS
                timeAnalysis = System.currentTimeMillis();
                StringBuilder sb = new StringBuilder();
                sb.append("[MPS] ");
                for (int i = 0; i < GameServer.peopleMax; i++) {
                    sb.append(String.valueOf(GameServer.connects[i].numberSend));
                    if (i != GameServer.peopleMax - 1) sb.append(" | ");
                    GameServer.connects[i].numberSend = 0;
                }
                log.trace(sb.toString());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

    }
}
