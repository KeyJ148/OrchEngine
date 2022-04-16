package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.location.Location;

import java.util.TreeMap;

public class ObjectsContainer {

	public final int borderSize = 100;
	private final int chunkSize = 100;
	private final java.util.Map<Integer, Layer> layerByZ = new TreeMap<>(); //Массив с DepthVector хранящий чанки TODO поправить комменты

	public int numberWidth;//Кол-во чанков
	public int numberHeight;
	public int chunkRender = 0;//Кол-во отрисованных чанков
	public int unsuitableObjectsRender = 0;//Кол-во отрисованных объектов, которые не поместились в чанки

	public ObjectsContainer(int width, int height) {
		int addCell = (int) Math.ceil((double) borderSize / chunkSize) * 2;//С двух сторон для каждой оси
		numberWidth = (int) (Math.ceil((double) width / chunkSize) + addCell);//+2 чанка минимум потому что надо обрабатывать вылет за карту
		numberHeight = (int) (Math.ceil((double) height / chunkSize) + addCell);//Желательно по 100-200 px на каждую сторону (Иначе на большой скорости можно пролететь границу)
	}

	public void add(GameObject gameObject) {
		if (gameObject.getLocation() != null){
			gameObject.getLocation().getMap().remove(gameObject);
		}

		gameObject.getLocationHolder().setLocation(location);

		int z = gameObject.getComponent(Position.class).z;
		layerByZ.computeIfAbsent(z, u -> new Layer(this, z)).add(gameObject);
	}

	public void del(GameObject gameObject) {
		int z = gameObject.getComponent(Position.class).z;
		layerByZ.get(z).del(gameObject);
	}

	public void clear() {
		layerByZ.clear();
	}

	public void update(long delta) {
		//TODO не заменяю на foreach, т.к. с ним ошибка:
        /*java.util.ConcurrentModificationException: null
            at java.util.Vector$Itr.checkForComodification(Vector.java:1298) ~[?:?]
            at java.util.Vector$Itr.next(Vector.java:1254) ~[?:?]
            at cc.abro.orchengine.location.map.Map.update(Map.java:23) ~[OrchEngine-0.1.0.jar:?]*/

		//TODO апдейтить по чанкам, которые в радиусе апдейта
		for (GameObject gameObject : objects) {
			gameObject.update(delta);
		}
	}

	public void update(GameObject gameObject) {
		int z = gameObject.getComponent(Position.class).z;
		if (!layerByZ.containsKey(z)){
			return;
		}

		layerByZ.get(z).update(gameObject);
	}

	//Отрисовка комнаты с размерами width и height вокруг координат (x;y)
	public void render(int x, int y, int width, int height, Camera camera) {
		background.render(x, y, width, height, camera);
		layersContainer.render(x, y, width, height);
	}

	public void render(int x, int y, int width, int height) {
		chunkRender = 0;
		unsuitableObjectsRender = 0;
		layerByZ.values().forEach(l -> l.render(x, y, width, height));
	}

	//Уничтожение всех объектов в комнате //TODO add "remove without destroy() all objects" method, и такой же метод в Location
	public void destroy() {
		for (GameObject gameObject : objects) {
			gameObject.destroy();
		}
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
