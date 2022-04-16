package cc.abro.orchengine.location.objects;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Movement;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.util.Vector2;

import java.util.*;
import java.util.Map;

public class Layer {

	private final int z;
	private final int chunkSize;
	private final ObjectsContainer mc; //TODO переименовать
	//Двумерный динамический массив хранит все Чанки TODO поправить все комменты в пакете map
	//Внешний массив хранит сортировку массивов по координате y
	//Внутренний массив имеет чанки с одинаковой y, но разными x
	private final Map<Vector2<Integer>, Chunk> chunks = new HashMap<>();
	//Объекты чьи текстура или маска больше размера чанка, и поэтому их необходимо обрабатывать всегда
	private final Set<GameObject> unsuitableObjects = new HashSet<>(); //TODO объекты добавляются вручную, а не определяются автоматически

	//TODO автоматически удалять неиспользуемые чанки? Обязательно надо, но когда? Когда ноль объектов? Вышли из зоны видимости?
	//TODO убрать ObjectContainer
	public Layer(ObjectsContainer mc, int z, int chunkSize) {
		this.mc = mc;
		this.z = z;
		this.chunkSize = chunkSize;
	}

	public void add(GameObject gameObject) {
		getOrCreateChunk((int) gameObject.getComponent(Position.class).x, (int) gameObject.getComponent(Position.class).y)
				.add(gameObject);
	}

	public void remove(GameObject gameObject) {
		if (!unsuitableObjects.contains(gameObject)) {
			getChunk((int) gameObject.getComponent(Position.class).x, (int) gameObject.getComponent(Position.class).y)
					.remove(gameObject);
		} else {
			unsuitableObjects.remove(gameObject);
		}
	}

	public int getZ() {
		return z;
	}

	//Обновление объекта при перемещении из чанка в чанк
	//TODO переименовать метод, указать что он про перемещение между чанками, мб убрать отсюда совсем
	//TODO убедиться, что не вызывается для unsuitable обхектов
	public void update(GameObject gameObject) {
		if (!gameObject.hasComponent(Movement.class)) return;

		Chunk chunkNow = getOrCreateChunk((int) gameObject.getComponent(Position.class).x, (int) gameObject.getComponent(Position.class).y);
		Chunk chunkLast = getChunk((int) gameObject.getComponent(Movement.class).getXPrevious(), (int) gameObject.getComponent(Movement.class).getYPrevious());

		if (!chunkLast.equals(chunkNow)) {
			chunkLast.remove(gameObject);
			chunkNow.add(gameObject);
		}
	}

	//TODO а нужна эта функция?
	public void update(long delta) {
		new HashSet<>(chunks.values()).forEach(chunk -> chunk.update(delta));
	}

	//TODO по идее лучше не использовать эту функцию, но мб и не удалять
	public Set<GameObject> getObjects() {
		Set<GameObject> allObjects = new HashSet<>();
		for (Chunk chunk : chunks.values()) {
			allObjects.addAll(chunk.getObjects());
		}
		return allObjects;
	}

	//TODO width и height при этом левая граница (как у массива), котоаря не включается!
	//TODO Указать это, и проверить, что так и работает, и в мапах ок (поиск по файлам)

	//Отрисовка чанков вокруг позиции x, y
	public void render(int x, int y, int width, int height) {
		//TODO по идее рендеринг надо делать для всех тех же объектов, для которых и update, т.к. мы не знаем их размер
		//width = 8000;
		//height = 8000;


		int renderChunksByAxisX = width / chunkSize + ((width % chunkSize == 0) ? 0 : 1);
		int renderChunksByAxisY = height / chunkSize + ((height % chunkSize == 0) ? 0 : 1);
		int renderSideChunksByAxisX = renderChunksByAxisX / 2;
		int renderSideChunksByAxisY = renderChunksByAxisY / 2;

		Vector2<Integer> renderChunkPos = getChunkPosition(x, y);
		for (int i = renderChunkPos.x - renderSideChunksByAxisX; i <= renderChunkPos.x + renderSideChunksByAxisX; i++) {
			for (int j = renderChunkPos.y - renderSideChunksByAxisY; j <= renderChunkPos.y + renderSideChunksByAxisY; j++) {
				Chunk renderingChunk = chunks.get(new Vector2<>(i, j));
				if (renderingChunk != null) {
					mc.chunkRender++;
					renderingChunk.render();
				}
			}
		}



		//Рендер объектов не помещающихся в чанке
		for (GameObject unsuitableGameObject : unsuitableObjects) {
			mc.unsuitableObjectsRender++;
			unsuitableGameObject.draw();
		}
	}

	private Chunk getChunk(int x, int y) {
		return chunks.get(getChunkPosition(x, y));
	}

	private Chunk getOrCreateChunk(int x, int y) {
		return chunks.computeIfAbsent(getChunkPosition(x, y), cp -> new Chunk());
	}

	private Vector2<Integer> getChunkPosition(int x, int y) {
		return new Vector2<>(x / chunkSize, y / chunkSize);
	}
}

