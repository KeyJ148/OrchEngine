package cc.abro.orchengine.location;

import cc.abro.orchengine.context.Context;
import cc.abro.orchengine.location.map.Camera;
import cc.abro.orchengine.location.map.Map;

public class Location {

	private final int width, height;
	private final Camera camera = new Camera(); //Положение камеры в этой локации

	private final Map map;
	private final GuiLocationFrame guiLocationFrame;

	public Location(int width, int height) {
		this.width = width;
		this.height = height;

		map = new Map(width, height, this);
		guiLocationFrame = new GuiLocationFrame();
	}

	public void update(long delta) {
		camera.update(); //Расчёт положения камеры
		map.update(delta);
		guiLocationFrame.update();
	}

	//Отрисовка комнаты с размерами width и height вокруг камеры
	public void render(int width, int height) {
		render((int) camera.getX(), (int) camera.getY(), width, height);
	}

	//Отрисовка комнаты с размерами width и height вокруг координат (x;y)
	public void render(int x, int y, int width, int height) {
		map.render(x, y, width, height, camera);
		guiLocationFrame.render();
	}

	//Удаление всех объектов в комнате
	public void destroy() {
		map.destroy();
		guiLocationFrame.destroy();
	}

	public boolean isActive(){
		return Context.getService(LocationManager.class).getActiveLocation() == this;
	}

	public Map getMap() {
		return map;
	}

	public GuiLocationFrame getGuiLocationFrame() {
		return guiLocationFrame;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Camera getCamera() {
		return camera;
	}
}
