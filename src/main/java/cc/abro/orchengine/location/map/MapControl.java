package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Position;

import java.util.Comparator;
import java.util.TreeMap;

public class MapControl {

	public final int borderSize = 100;
	private final int chunkSize = 100;
	private final java.util.Map<Integer, DepthVector> depthVectorByDepth = new TreeMap<>(Comparator.reverseOrder()); //Массив с DepthVector хранящий чанки

	public int numberWidth;//Кол-во чанков
	public int numberHeight;
	public int chunkRender = 0;//Кол-во отрисованных чанков

	public MapControl(int width, int height) {
		int addCell = (int) Math.ceil((double) borderSize / chunkSize) * 2;//С двух сторон для каждой оси
		numberWidth = (int) (Math.ceil((double) width / chunkSize) + addCell);//+2 чанка минимум потому что надо обрабатывать вылет за карту
		numberHeight = (int) (Math.ceil((double) height / chunkSize) + addCell);//Желательно по 100-200 px на каждую сторону (Иначе на большой скорости можно пролететь границу)
	}

	public void add(GameObject gameObject) {
		int depth = gameObject.getComponent(Position.class).depth;
		depthVectorByDepth.computeIfAbsent(depth, u -> new DepthVector(this, depth)).add(gameObject);
	}

	public void del(GameObject gameObject) {
		int depth = gameObject.getComponent(Position.class).depth;
		depthVectorByDepth.get(depth).del(gameObject);
	}

	public void clear() {
		depthVectorByDepth.clear();
	}

	public void update(GameObject gameObject) {
		int depth = gameObject.getComponent(Position.class).depth;
		if (!depthVectorByDepth.containsKey(depth)){
			return;
		}

		depthVectorByDepth.get(depth).update(gameObject);
	}

	public void render(int x, int y, int width, int height) {
		chunkRender = 0;
		depthVectorByDepth.values().forEach(dv -> dv.render(x, y, width, height));
	}

	public int getCountDepthVectors() {
		return depthVectorByDepth.size();
	}

	public int getChunkSize() {
		return chunkSize;
	}
}
