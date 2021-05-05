package cc.abro.orchengine.net.server.readers;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.net.server.GameServer;
import cc.abro.orchengine.net.server.MessagePack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ServerReadTCP extends Thread {

    private static final Logger log = LogManager.getLogger(ServerReadTCP.class);

    private int id; //номер соединения в массиве в gameServer

    public ServerReadTCP(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        //Постоянный обмен данными (на TCP)
        //Только после подключения всех игроков
        String str;
        try {
            while (true) {
                str = GameServer.connects[id].in.readUTF();
                synchronized (GameServer.connects[id].messagePack) {//Защита от одновременной работы с массивом
                    GameServer.connects[id].messagePack.add(str, MessagePack.Message.InetType.TCP);
                }
            }
        } catch (IOException e) {
            log.info("Player disconnect (id: " + id + ")");
            GameServer.disconnect++;
            GameServer.connects[id].disconnect = true;
        }
    }

}