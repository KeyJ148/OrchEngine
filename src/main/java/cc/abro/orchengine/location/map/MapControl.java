package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Position;

import java.util.TreeMap;

public class MapControl {

	public final int borderSize = 100;
	private final int chunkSize = 100;
	private final java.util.Map<Integer, Layer> layerByZ = new TreeMap<>(); //Массив с DepthVector хранящий чанки TODO поправить комменты

	public int numberWidth;//Кол-во чанков
	public int numberHeight;
	public int chunkRender = 0;//Кол-во отрисованных чанков
	public int unsuitableObjectsRender = 0;//Кол-во отрисованных объектов, которые не поместились в чанки

	public MapControl(int width, int height) {
		int addCell = (int) Math.ceil((double) borderSize / chunkSize) * 2;//С двух сторон для каждой оси
		numberWidth = (int) (Math.ceil((double) width / chunkSize) + addCell);//+2 чанка минимум потому что надо обрабатывать вылет за карту
		numberHeight = (int) (Math.ceil((double) height / chunkSize) + addCell);//Желательно по 100-200 px на каждую сторону (Иначе на большой скорости можно пролететь границу)
	}

	public void add(GameObject gameObject) {
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

	public void update(GameObject gameObject) {
		int z = gameObject.getComponent(Position.class).z;
		if (!layerByZ.containsKey(z)){
			return;
		}

		layerByZ.get(z).update(gameObject);
	}

	public void render(int x, int y, int width, int height) {
		chunkRender = 0;
		unsuitableObjectsRender = 0;
		layerByZ.values().forEach(l -> l.render(x, y, width, height));
	}

	public int getCountLayers() {
		return layerByZ.size();
	}

	public int getChunkSize() {
		return chunkSize;
	}
}
