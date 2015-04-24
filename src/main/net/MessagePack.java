package main.net;

import java.util.ArrayList;

public class MessagePack {
	
	public ArrayList<String> message;//������ ���������
	
	public MessagePack(){
		this.message = new ArrayList<String>();
	}
	
	public void add(String str){
		message.add(str);
	}
	
	public String get(){
		if (size()%100 == 0) System.out.println("Messages detained: " + size());
		return message.remove(0);
	}
	
	public int size(){
		return message.size();
	}
	
	public boolean haveMessage(){
		//message.trimToSize();
		if (message.size() > 0){
			if(message.get(0) != null){
				return true;
			} else {
				message.remove(0);
			}
		}
		return false;
	}	
}
