package engine.net.client;

import engine.Loader;
import engine.io.Logger;
import engine.setting.SettingStorage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPControl {
	
	private DataInputStream in;
	private DataOutputStream out;

	public int sizeDataSend = 0;//bytes 
	public int sizeDataRead = 0;
	public Object sizeDataReadMonitor = new Object();//Нужен, чтобы не запрашивать sizeDataRead из потока считывания и анализатора
	
	public void connect(String ip, int port){
		try{
			@SuppressWarnings("resource")
			Socket sock = new Socket(InetAddress.getByName(ip), port);
			sock.setTcpNoDelay(SettingStorage.Net.TCP_NODELAY);
			sock.setKeepAlive(SettingStorage.Net.KEEP_ALIVE);
			sock.setSendBufferSize(SettingStorage.Net.SEND_BUF_SIZE);
			sock.setReceiveBufferSize(SettingStorage.Net.RECEIVE_BUF_SIZE);
			sock.setPerformancePreferences(SettingStorage.Net.PREFERENCE_CON_TIME, SettingStorage.Net.PREFERENCE_LATENCY, SettingStorage.Net.PREFERENCE_BANDWIDTH);
			sock.setTrafficClass(SettingStorage.Net.TRAFFIC_CLASS);
			
			InputStream inS = sock.getInputStream();
			OutputStream outS = sock.getOutputStream();
			DataInputStream in = new DataInputStream(inS);
			DataOutputStream out = new DataOutputStream(outS);
			
			this.in = in;
			this.out = out;
		} catch(IOException e){
			Logger.println("Connection failed", Logger.Type.ERROR);
			Loader.exit();
		}
	}
	
	public void send(int type, String str){
		try{
			sizeDataSend += str.length()*2;
			if (out != null) {
				out.flush();
				out.writeUTF(type + " " + str);
			}
		} catch (IOException e){
			Logger.println("Connection lost (Rend)", Logger.Type.ERROR);
			Loader.exit();
		}
	}
	
	public String read(){
		try{
			String str = in.readUTF();
			synchronized (sizeDataReadMonitor){
				sizeDataRead += str.length()*2;
			}
			return str;
		} catch (IOException e){
			Logger.println("Connection lost (Ыутв)", Logger.Type.ERROR);
			Loader.exit();
			return null;
		}
	}
}