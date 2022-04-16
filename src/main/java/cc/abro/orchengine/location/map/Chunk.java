package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;

import java.util.HashSet;
import java.util.Set;

public class Chunk {

	private final Set<GameObject> objects = new HashSet<>(); //Объекты которые надо отрисовать //TODO set?
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
		objects.remove(gameObject);
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
