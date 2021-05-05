package cc.abro.orchengine.net.client.udp;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Loader;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.net.NetTools;
import cc.abro.orchengine.net.client.NetControl;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPControl extends NetControl {

    private static final Logger log = LogManager.getLogger(UDPControl.class);

    private DatagramSocket socket;
    private String ip;
    private int port;

    public void connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            socket = new DatagramSocket();
            socket.setSendBufferSize(SettingsStorage.NETWORK.SEND_BUF_SIZE);
            socket.setReceiveBufferSize(SettingsStorage.NETWORK.RECEIVE_BUF_SIZE);
            socket.setTrafficClass(SettingsStorage.NETWORK.TRAFFIC_CLASS);
        } catch (IOException e) {
            log.warn("Connection failed (UDP)");
            Loader.exit();
        }
    }

    @Override
    public void send(int type, String str) {
        try {
            byte[] data = (type + " " + str).getBytes();
            InetAddress addr = InetAddress.getByName(ip);

            if (socket != null) {
                DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
                socket.send(packet);

                analyzeSend(data.length);
            }
        } catch (IOException e) {
            log.warn("Connection lost (UDP send)");
            Loader.exit();
        }
    }

    @Override
    public String read() {
        int size = SettingsStorage.NETWORK.UDP_READ_BYTE_ARRAY_LEN;

        try {
            DatagramPacket packet = new DatagramPacket(new byte[size], size);
            socket.receive(packet);

            byte[] data = NetTools.clearByteData(packet.getData());
            analyzeRead(data.length);
            return new String(data);
        } catch (IOException e) {
            log.warn("Connection lost (TCP read)");
            Loader.exit();
            return null;
        }
    }
}
