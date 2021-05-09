package cc.abro.orchengine.net.server.senders;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.net.server.GameServer;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Log4j2
public class ServerSendTCP {

    public static void send(int id, int type, String str) {
        if (!GameServer.connects[id].disconnect) {
            try {
                synchronized (GameServer.connects[id].out) {
                    GameServer.connects[id].out.writeUTF(type + " " + str);
                    GameServer.connects[id].out.flush();
                }
                GameServer.connects[id].numberSend++; //Кол-во отправленных пакетов
            } catch (IOException e) {
                log.warn("Send message failed (TCP)");
            }
        }
    }

    public static void sendAllExceptId(int id, int type, String str) {
        for (int i = 0; i < GameServer.peopleMax; i++) {//Отправляем сообщение всем
            if (i != id) {//Кроме игрока, приславшего сообщение
                send(i, type, str);
            }
        }
    }

    public static void sendAll(int type, String str) {
        for (int i = 0; i < GameServer.peopleMax; i++) {//Отправляем сообщение всем
            send(i, type, str);
        }
    }

}
