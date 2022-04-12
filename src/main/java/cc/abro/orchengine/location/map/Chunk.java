package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;

import java.util.ArrayList;

public class Chunk {

	private final ArrayList<GameObject> objects = new ArrayList<>(); //Объекты которые надо отрисовать //TODO set?
	private final int posWidth; //Порядковый номер чанка
	private final int posHeight;

	public Chunk(int posWidth, int posHeight) {
		this.posWidth = posWidth;
		this.posHeight = posHeight;
	}

	public void add(GameObject gameObject) {
		objects.add(gameObject);
	}

	public void del(GameObject gameObject) {
		for (int j = 0; j < objects.size(); j++) {
			if (objects.get(j) == gameObject) {
				objects.remove(j);
				break;
			}
		}
	}

	public int size() {
		return objects.size();
	}

	public void render() {
		for (GameObject gameObject : objects) {
			gameObject.draw();
		}
	}

	public int getPosHeight() {
		return posHeight;
	}

	public int getPosWidth() {
		return posWidth;
	}
}
