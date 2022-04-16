package cc.abro.orchengine.location.objects;

import cc.abro.orchengine.gameobject.GameObject;

import java.util.HashSet;
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
		new HashSet<>(objects).forEach(gameObject -> gameObject.update(delta));
	}

	public void render() {
		for (GameObject gameObject : objects) {
			gameObject.draw();
		}
	}

	//TODO по идее лучше не использовать эту функцию, но мб и не удалять (как минимум не копировать массив!)
	public Set<GameObject> getObjects() {
		return objects;
	}

	public int size() {
		return objects.size();
	}
}
