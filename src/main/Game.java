package main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;

import main.net.LinkCS;
import main.player.enemy.EnemyBullet;

public class Game extends Canvas implements Runnable{
	
	public static final int TPS = 100; //���-�� ���������� update � �������
	public static final int SKIP_TICKS = 1000/TPS;
	public static final int MAX_FRAME_SKIP = 10;
	public static final boolean console = true;//�������� � ������� ��������� �������?
	public static final boolean consoleFPS = false;//�������� � ������� ���?
	public static final boolean monitorFPS = true;//�������� � ���� ���?
	
	public static int WIDTH = 800;
	public static int HEIGHT = 600;//������ ����
	public static String WINDOW_NAME = "Game";

	public boolean running = false; //���������� �������� �������� �����
	public String monitorStrFPS = "";
	
	public String name;//��� ���������
	public DataInputStream in;//������ ������� ����
	public DataOutputStream out;
	
	public int heightMap;
	public int widthMap;//������ �����
	public int peopleMax;//���-�� ������� �� �����
	
	private static final long serialVersionUID = 1L; //�������������
	
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	public void run(){
		long nextLoops = System.currentTimeMillis();
		int loopsUpdate;
		int loopsRender = 0; //��� �������� fps
		int loopsRenderMid = 0;
		int second = 0;
		long fps_t = System.currentTimeMillis(); //��� �������� fps
		init();
        
		while(running) { //������� ������� ����
			loopsUpdate = 0;
			while ((System.currentTimeMillis() > nextLoops) && (loopsUpdate < MAX_FRAME_SKIP)) {
				update();
				
				nextLoops += SKIP_TICKS;
				loopsUpdate++;
			}
			render();
			loopsRender++;
			if (System.currentTimeMillis() >= fps_t + 1000){
				second++;
				loopsRenderMid += loopsRender;
				if ((Game.consoleFPS) || (Game.monitorFPS)) {
					int objSize = 0;
					for (int i=0;i<Global.obj.size();i++){
						if (Global.obj.get(i) != null){
							objSize++;
						}
					}
					if (Game.consoleFPS) System.out.println("FPS: " + loopsRender + "          MidFPS: " + loopsRenderMid/second + "          Object: " + objSize);
					if (Game.monitorFPS) this.monitorStrFPS = "FPS: " + loopsRender + "          MidFPS: " + loopsRenderMid/second + "          Object: " + objSize;
				}
				fps_t = System.currentTimeMillis();
				loopsRender = 0;
			}
		}
	}
	
	//������������� ����� ��������
	public void init() {
		if (Game.console) System.out.println("Inicialization start.");
		Global.obj = new ArrayList<Obj>();
		Global.depth = new ArrayList<DepthVector>();
		Global.enemyBullet = new ArrayList<EnemyBullet>();
		Global.linkCS = new LinkCS();
		
		Global.linkCS.initSprite();
		
		Global.clientThread.initMap(this);
		
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		if (Game.console) System.out.println("Inicialization end.");
	}
    
    //��������� ������ (� �������� fps)
	public void render() {
		//��������� ������� �����������
		BufferStrategy bs = getBufferStrategy(); 
		if (bs == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}
        //��������� ����
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setColor(new Color(24,116,0));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//������� ����
		Image back = Global.background.getImage();
		int dxf,dyf;
		for (int dy = 0; dy<=this.heightMap; dy+=64){
			for (int dx = 0; dx<=this.widthMap; dx+=64){
				dxf = (int) Math.round(Global.cameraXView - (Global.cameraX - dx));
				dyf = (int) Math.round( Global.cameraYView - (Global.cameraY - dy));
				g.drawImage(back,dxf,dyf,null);
			}
		}
		
		//���������� �������� �� ������� �������� � ���
		for (int i=0; i<Global.depth.size(); i++){
			for (int j=0; j<Global.depth.size()-1; j++){
				DepthVector dv1 = (DepthVector) Global.depth.get(j);
				DepthVector dv2 = (DepthVector) Global.depth.get(j+1);
				if (dv1.depth < dv2.depth){
					Global.depth.set(j, dv2);
					Global.depth.set(j+1, dv1);
				}
			}
		}
		
		//��������� ��������		
		for (int i=0; i<Global.depth.size(); i++){
			DepthVector dv = (DepthVector) Global.depth.get(i);
			for (int j=0; j<dv.number.size(); j++){
				if (Global.obj.get(i) != null){
					Obj obj = (Obj) Global.getObj((long) dv.number.get(j));
					obj.draw(g);
				}
			}
		}
		
		//��������� ��������
		AffineTransform at = new AffineTransform(); 
		g.setTransform(at);
		g.setColor(new Color(0,0,0));
		g.setFont(new Font(null,Font.PLAIN,12));
		g.drawString(name,Global.player.nameX,Global.player.nameY);
		for(int i = 0;i<Global.enemy.length;i++){
			try{
				g.drawString(Global.enemy[i].name,Global.enemy[i].nameX,Global.enemy[i].nameY);
			}catch(NullPointerException e){}
		}
		g.setFont(new Font(null,Font.BOLD,20));
		long hp = Math.round(Global.player.getArmor().getHp());
		long hpMax = Math.round(Global.player.getArmor().getHpMax());
		g.drawString("HP: " + hp + "/" + hpMax,1,16);
		if (monitorFPS){
			g.setFont(new Font(null,Font.PLAIN,12));
			g.drawString(monitorStrFPS,1,HEIGHT+9);
		}
		
		//����� [ON]
		g.dispose();
		bs.show();
		//����� [OFF]
	}
    
    //���������� �������� (TPS)
	public void update(){
		for (int i=0; i<Global.obj.size(); i++){
			if (Global.obj.get(i) != null){
				Obj obj = (Obj) Global.obj.get(i);
				obj.update();
			}
		}
	}
	
	//�������� ���� � ������ ����
	public static void main (String args[]) {
		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		JFrame frame = new WindowMain(WINDOW_NAME, game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		
		if (Game.console) System.out.println("Window create.");
	}
}
