package cc.abro.orchengine.net.server.senders;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.net.server.GameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class ServerSendUDP {

    private static final Logger log = LogManager.getLogger(ServerSendUDP.class);

    public static void send(int id, int type, String str) {
        if (!GameServer.connects[id].disconnect) {
            try {
                byte[] data = (type + " " + str).getBytes();
                InetAddress addr = InetAddress.getByName(GameServer.connects[id].ipRemote);
                int port = GameServer.connects[id].portUDP;

                DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
                GameServer.socketUDP.send(packet);

                GameServer.connects[id].numberSend++; //Кол-во отправленных пакетов
            } catch (IOException e) {
                log.warn("Send message failed (UDP)");
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
