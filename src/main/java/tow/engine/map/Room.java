package tow.engine.map;

import tow.engine.obj.Obj;

import java.util.Vector;

public class Room {

	public Background background;
	public int width, height;

	public Vector<Obj> objects; //Массив со всеми объектами
	public MapControl mapControl; //Массив со всеми чанками и объектами
	public Camera camera;

	public Room(int width, int height) {
		this.width = width;
		this.height = height;
		this.background = new Background();

		objects = new Vector<>();
		mapControl = new MapControl(width, height);
		camera = new Camera();
	}

	public void update(long delta){
		camera.update();//Расчёт положения камеры

		for (int i=0; i<objects.size(); i++){
			Obj obj = objects.get(i);
			if (obj != null) obj.update(delta);
		}

		for (Obj obj : objects) {
			if (obj != null) obj.updateFollow();
		}
	}

	//Отрисовка комнаты с размерами width и height вокруг камеры
	public void render(int width, int height){
		render((int) camera.getX(), (int) camera.getY(), width, height);
	}

	//Отрисовка комнаты с размерами width и height вокруг координат (x;y)
	public void render(int x, int y, int width, int height){
		background.render(x, y, width, height, camera);
		mapControl.render(x, y, width, height);
	}

	public int objCount(){
		int count = 0;
		for (Obj obj : objects)
			if (obj != null)
				count++;

		return count;
	}

	//Добавление объекта в комнату
	public void objAdd(Obj obj){
		obj.position.id = objects.size();
		obj.position.room = this;
		obj.destroy = false;

		objects.add(obj);
		mapControl.add(obj);
	}

	//Удаление объекта из комнаты по id
	public void objDel(int id){
		mapControl.del(id);//Используется objects, так что должно быть раньше
		objects.set(id, null);
	}

	//Удаление всех объектов в комнате
	public void destroy() {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) != null) objects.get(i).destroy();
		}
	}

}
