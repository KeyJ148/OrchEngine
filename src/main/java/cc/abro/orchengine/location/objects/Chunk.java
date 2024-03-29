package cc.abro.orchengine.location.objects;

import cc.abro.orchengine.gameobject.GameObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chunk {

	public static final int DEFAULT_SIZE = 100;

	private final Set<GameObject> objects = new HashSet<>();

	public void add(GameObject gameObject) {
		objects.add(gameObject);
	}

	public void remove(GameObject gameObject) {
		objects.remove(gameObject);
	}

	public void update(long delta) {
		//Делаем копию сета, иначе получаем ConcurrentModificationException,
		//т.к. во время апдейта можно создать новый объект и этот объект будет помещен в сет
		new ArrayList<>(objects).forEach(gameObject -> gameObject.update(delta));
	}

	public void render() {
		for (GameObject gameObject : objects) {
			gameObject.draw();
		}
	}

	public void destroy() {
		for (GameObject gameObject : objects) {
			gameObject.destroy();
		}
	}

	@Deprecated
	public List<GameObject> getObjects() {
		return new ArrayList<>(objects);
	}

	public int size() {
		return objects.size();
	}
}
