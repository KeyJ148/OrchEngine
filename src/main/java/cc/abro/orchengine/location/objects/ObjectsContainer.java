package cc.abro.orchengine.location.objects;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.location.Location;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ObjectsContainer {

	private final int chunkSize;
	private final Location location;
	private final Map<Integer, Layer> layerByZ = new TreeMap<>();

	//TODO поправить комменты в этом пакете
	//TODO статистику в другое место, а лучше через листенеры и т.п.
	public int chunkRender = 0;//Кол-во отрисованных чанков
	public int unsuitableObjectsRender = 0;//Кол-во отрисованных объектов, которые не поместились в чанки

	//TODO поддержка отрицательных координат для x и y
	//TODO обработка вылета за карту через листенер пересечения границы карты в классе Movement или типа того
	//TODO также добавить бордреры в Location и в Collision систему события о столкновения с бордерами, сделать их очень широкими?
	public ObjectsContainer(int width, int height, int chunkSize, Location location) {
		this.chunkSize = chunkSize;
		this.location = location;
	}

	public void add(GameObject gameObject) {
		if (gameObject.getLocation() != null){
			gameObject.getLocation().getObjectsContainer().remove(gameObject);
		}
		gameObject.getLocationHolder().setLocation(location);

		int z = gameObject.getComponent(Position.class).z;
		layerByZ.computeIfAbsent(z, u -> new Layer(this, z, chunkSize)).add(gameObject);
	}

	public void remove(GameObject gameObject) {
		int z = gameObject.getComponent(Position.class).z;
		layerByZ.get(z).remove(gameObject);
	}

	//TODO нужна функция ? Или destroy?
	public void clear() {
		layerByZ.clear();
	}

	public void update(long delta) {
		//TODO апдейтить по чанкам, которые в радиусе апдейта, но возможно несколько областей апдейта
		//TODO делаем копию значений, иначе получаем ConcurrentModificationException
		new HashSet<>(layerByZ.values()).forEach(layer -> layer.update(delta));
	}

	public void update(GameObject gameObject) {
		if (gameObject.getLocation() != location){
			return;
		}

		int z = gameObject.getComponent(Position.class).z;
		layerByZ.get(z).update(gameObject);
	}

	//Отрисовка комнаты с размерами width и height вокруг координат (x;y)
	public void render(int x, int y, int width, int height) {
		chunkRender = 0;
		unsuitableObjectsRender = 0;
		layerByZ.values().forEach(l -> l.render(x, y, width, height));
	}

	//Уничтожение всех объектов в комнате //TODO add "remove without destroy() all objects" method, и такой же метод в Location
	public void destroy() {
		/*TODO for (GameObject gameObject : objects) {
			gameObject.destroy();
		}*/
	}

	//TODO по идее лучше не использовать эту функцию, но мб и не удалять
	public Set<GameObject> getObjects() {
		Set<GameObject> allObjects = new HashSet<>();
		for (Layer layer : layerByZ.values()) {
			allObjects.addAll(layer.getObjects());
		}
		return allObjects;
	}

	public int getCountLayers() {
		return layerByZ.size();
	}

	public int getChunkSize() {
		return chunkSize;
	}

	/**
	 * Используется, чтобы только из данного класса можно было вызывать setLocation у игровых объектов
	 */
	public static final class LocationObjectHolder {

		private Location location;

		public Location getLocation() {
			return location;
		}

		private void setLocation(Location location) {
			this.location = location;
		}
	}
}
