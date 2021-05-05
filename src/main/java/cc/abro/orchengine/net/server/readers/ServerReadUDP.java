package cc.abro.orchengine.net.server.readers;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Loader;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.net.NetTools;
import cc.abro.orchengine.net.server.Connect;
import cc.abro.orchengine.net.server.GameServer;
import cc.abro.orchengine.net.server.MessagePack;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;

public class ServerReadUDP extends Thread {

    private static final Logger log = LogManager.getLogger(ServerReadUDP.class);

    @Override
    public void run() {
        //Постоянный обмен данными (на UDP)
        //Только после подключения всех игроков
        String str, ipSender;
        int portSender;
        try {
            int size = SettingsStorage.NETWORK.UDP_READ_BYTE_ARRAY_LEN;
            ;

            while (true) {
                //Ждем сообщение
                DatagramPacket packet = new DatagramPacket(new byte[size], size);
                GameServer.socketUDP.receive(packet);

                //Получаем информацию из сообщения
                str = new String(NetTools.clearByteData(packet.getData()));
                ipSender = packet.getAddress().getHostAddress();
                portSender = packet.getPort();

                //Находим игрока-отправителя
                for (Connect connect : GameServer.connects) {
                    if (connect.ipRemote.equals(ipSender) && connect.portUDP == portSender) {
                        //Добавляем в очередь сообщений
                        synchronized (connect.messagePack) {//Защита от одновременной работы с массивом
                            connect.messagePack.add(str, MessagePack.Message.InetType.UDP);
                        }

                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.warn("UDP server socket closed");
            Loader.exit();
        }
    }
}