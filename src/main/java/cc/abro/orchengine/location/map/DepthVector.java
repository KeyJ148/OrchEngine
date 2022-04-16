package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Collision;
import cc.abro.orchengine.gameobject.components.Movement;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.gameobject.components.render.Rendering;
import cc.abro.orchengine.util.Vector2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DepthVector {

	private final int depth;
	private final MapControl mc;
	//Двумерный динамический массив хранит все Чанки
	//Внешний массив хранит сортировку массивов по координате y
	//Внутренний массив имеет чанки с одинаковой y, но разными x
	private final Map<Vector2<Integer>, Chunk> chunks = new HashMap<>();
	//Объекты чьи текстура или маска больше размера чанка, и поэтому их необходимо обрабатывать всегда
	private final Set<GameObject> unsuitableObjects = new HashSet<>();

	public DepthVector(MapControl mc, int depth) {
		this.mc = mc;
		this.depth = depth;
	}

	public void add(GameObject gameObject) {
		if (isObjectSmallerThenChunk(gameObject)) {
			getOrCreateChunk((int) gameObject.getComponent(Position.class).x, (int) gameObject.getComponent(Position.class).y)
					.add(gameObject);
		} else {
			unsuitableObjects.add(gameObject);
		}
	}

	public void del(GameObject gameObject) {
		if (!unsuitableObjects.contains(gameObject)) {
			getChunk((int) gameObject.getComponent(Position.class).x, (int) gameObject.getComponent(Position.class).y)
					.del(gameObject);
		} else {
			unsuitableObjects.remove(gameObject);
		}
	}

	public int getDepth() {
		return depth;
	}

	//Обновление объекта при перемещение из чанка в чанк
	public void update(GameObject gameObject) {
		if (!gameObject.hasComponent(Movement.class)) return;

		Chunk chunkNow = getOrCreateChunk((int) gameObject.getComponent(Position.class).x, (int) gameObject.getComponent(Position.class).y);
		Chunk chunkLast = getChunk((int) gameObject.getComponent(Movement.class).getXPrevious(), (int) gameObject.getComponent(Movement.class).getYPrevious());

		if (!chunkLast.equals(chunkNow)) {
			chunkLast.del(gameObject);
			chunkNow.add(gameObject);
		}
	}

	//Отрисовка чанков вокруг позиции x, y
	public void render(int x, int y, int width, int height) {
		Vector2<Integer> renderChunkPos = getPoint(x, y);
		int rangeX = (int) Math.ceil((double) width / 2 / mc.getChunkSize())+2;//В чанках
		int rangeY = (int) Math.ceil((double) height / 2 / mc.getChunkSize())+2;//В чанках

		for (int i = renderChunkPos.x - rangeX; i <= renderChunkPos.x + rangeX; i++) {
			for (int j = renderChunkPos.y - rangeY; j <= renderChunkPos.y + rangeY; j++) {
				if ((i >= 0) && (i < mc.numberWidth) && (j >= 0) && (j < mc.numberHeight)) {
					Chunk renderingChunk = chunks.get(new Vector2<>(i, j));
					if (renderingChunk != null) {
						mc.chunkRender++;
						renderingChunk.render();
					}
				}
			}
		}

		//Рендер объектов не помещающихся в чанке
		for (GameObject unsuitableGameObject : unsuitableObjects) {
			unsuitableGameObject.draw();
		}
	}

	private boolean isObjectSmallerThenChunk(GameObject gameObject){
		boolean isSmaller = true;
		if (gameObject.hasComponent(Rendering.class)) {
			isSmaller &= gameObject.getComponent(Rendering.class).getWidth() < mc.getChunkSize() &&
					gameObject.getComponent(Rendering.class).getHeight() < mc.getChunkSize();
		}
		if (gameObject.hasComponent(Collision.class)) {
			isSmaller &= gameObject.getComponent(Collision.class).getMask().getWidth() < mc.getChunkSize() &&
					gameObject.getComponent(Collision.class).getMask().getHeight() < mc.getChunkSize();
		}
		return isSmaller;
	}

	private Chunk getChunk(int x, int y) {
		return chunks.get(getPoint(x, y));
	}

	private Chunk getOrCreateChunk(int x, int y) {
		Vector2<Integer> chunkPosition = getPoint(x, y);
		return chunks.computeIfAbsent(chunkPosition, cp -> new Chunk(cp.x, cp.y));
	}

	private Vector2<Integer> getPoint(int x, int y) {
		int delta = mc.borderSize / mc.getChunkSize() - 1;//delta=0 (1-1)
		int posWidth = (int) Math.ceil((double) x / mc.getChunkSize() + delta);//-1 т.к. нумерация в массиве с 0
		int posHeight = (int) Math.ceil((double) y / mc.getChunkSize() + delta);//+1 т.к. добавлена обводка карты толщиной в 1 чанк для обработки выхода за карту
		return new Vector2<>(posWidth, posHeight);
	}
}

