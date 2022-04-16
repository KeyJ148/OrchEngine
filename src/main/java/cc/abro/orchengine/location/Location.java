package cc.abro.orchengine.location;

import cc.abro.orchengine.location.objects.Background;
import cc.abro.orchengine.location.objects.Camera;
import cc.abro.orchengine.location.objects.Chunk;
import cc.abro.orchengine.location.objects.ObjectsContainer;

public class Location {

	private final int width, height;
	private final Camera camera = new Camera(); //Положение камеры в этой локации
	private final ObjectsContainer objectsContainer; //Массив со всеми чанками и объектами
	private final GuiLocationFrame guiLocationFrame;

	private Background background; //Фон карты (цвет и текстура)

	public Location() {
		this(Integer.MAX_VALUE, Integer.MAX_VALUE, Chunk.DEFAULT_SIZE);
	}

	public Location(int chunkSize) {
		this(Integer.MAX_VALUE, Integer.MAX_VALUE, chunkSize);
	}

	public Location(int width, int height) {
		this(width, height, Chunk.DEFAULT_SIZE);
	}

	public Location(int width, int height, int chunkSize) {
		this.width = width;
		this.height = height;

		background = new Background();
		objectsContainer = new ObjectsContainer(width, height, chunkSize, this);
		guiLocationFrame = new GuiLocationFrame();
	}

	public void update(long delta) {
		camera.update(); //Расчёт положения камеры
		objectsContainer.update(delta);
		guiLocationFrame.update();
	}

	//Отрисовка комнаты с размерами width и height вокруг камеры TODO слово комната поменять на локация везде
	public void render(int width, int height) {
		render((int) camera.getX(), (int) camera.getY(), width, height);
	}

	//Отрисовка комнаты с размерами width и height вокруг координат (x;y)
	public void render(int x, int y, int width, int height) {
		background.render(x, y, width, height, camera);
		objectsContainer.render(x, y, width, height);
		guiLocationFrame.render();
	}

	//Удаление всех объектов в комнате
	public void destroy() {
		objectsContainer.destroy();
		guiLocationFrame.destroy();
	}

	public Background getBackground() {
		return background;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public ObjectsContainer getObjectsContainer() {
		return objectsContainer;
	}

	public GuiLocationFrame getGuiLocationFrame() {
		return guiLocationFrame;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Camera getCamera() {
		return camera;
	}
}
