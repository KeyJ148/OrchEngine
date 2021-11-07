package cc.abro.orchengine.map;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.services.GuiService;
import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.gui.input.keyboard.KeyboardHandler;
import cc.abro.orchengine.gui.input.mouse.MouseHandler;
import org.liquidengine.legui.component.Frame;

import java.util.Vector;

public class Location {

	public final int width, height;
	public Background background = new Background(); //Фон карты (цвет и текстура)
	public Camera camera = new Camera(); //Положение камеры в этой локации

	private Vector<GameObject> objects = new Vector<>(); //Массив со всеми объектами
	public MapControl mapControl; //Массив со всеми чанками и объектами

	private final Frame guiFrame; //Объект хранящий все элементы gui в данной комнате
	protected KeyboardHandler keyboard; //Объект хранящий события клавитуры
	protected MouseHandler mouse; //Объект хранящий события мыши и рисующий курсор на экране

	public Location(int width, int height) {
		this.width = width;
		this.height = height;
		mapControl = new MapControl(width, height);

		guiFrame = Manager.getService(GuiService.class).createFrame();
	}

	public void pollEvents() {
		Manager.getService(GuiService.class).pollEvents(getGuiFrame());
	}

	public void update(long delta) {
		camera.update();//Расчёт положения камеры

		for (int i = 0; i < objects.size(); i++) {
			GameObject gameObject = objects.get(i);
			if (gameObject != null) gameObject.update(delta);
		}
	}

	//Отрисовка комнаты с размерами width и height вокруг камеры
	public void render(int width, int height) {
		render((int) camera.getX(), (int) camera.getY(), width, height);
	}

	//Отрисовка комнаты с размерами width и height вокруг координат (x;y)
	public void render(int x, int y, int width, int height) {
		background.render(x, y, width, height, camera);
		mapControl.render(x, y, width, height);

		Manager.getService(GuiService.class).render(getGuiFrame()); //Отрисовка интерфейса (LeGUI)
		getMouse().draw(); //Отрисовка курсора мыши
	}

	public int objCount() {
		int count = 0;
		for (GameObject gameObject : objects)
			if (gameObject != null)
				count++;

		return count;
	}

	public GameObject getObject(int id) {
		return objects.get(id);
	}

	public int getObjectsVectorSize() {
		return objects.size();
	}

	public Vector<GameObject> getObjects() {
		return new Vector<>(objects);
	}

	//Добавление объекта в комнату
	public void objAdd(GameObject gameObject) {
		if (gameObject.isDestroy()) throw new IllegalArgumentException("Obj was destroy");

		gameObject.getComponent(Position.class).id = objects.size();
		gameObject.getComponent(Position.class).location = this;

		objects.add(gameObject);
		mapControl.add(gameObject);
	}

	//Удаление объекта из комнаты по id
	public void objDel(int id) {
		mapControl.del(id);//Используется objects, так что должно быть раньше
		objects.set(id, null);
	}

	//Удаление всех объектов в комнате
	public void destroy() {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) != null) objects.get(i).destroy();
		}
	}

	public boolean isActive(){
		return Manager.getService(LocationManager.class).getActiveLocation() == this;
	}

	public Frame getGuiFrame() {
		return guiFrame;
	}

	public KeyboardHandler getKeyboard() {
		return keyboard;
	}

	public MouseHandler getMouse() {
		return mouse;
	}
}
