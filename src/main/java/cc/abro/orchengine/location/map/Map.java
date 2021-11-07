package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.location.Location;

import java.util.Vector;

public class Map {

    public Background background = new Background(); //Фон карты (цвет и текстура)
    private Vector<GameObject> objects = new Vector<>(); //Массив со всеми объектами
    public MapControl mapControl; //Массив со всеми чанками и объектами

    private final Location location;//TODO del from this class

    public Map(int width, int height, Location location) {
        this.location = location;
        mapControl = new MapControl(width, height);
    }

    public void update(long delta) {
        for (int i = 0; i < objects.size(); i++) {
            GameObject gameObject = objects.get(i);
            if (gameObject != null) gameObject.update(delta);
        }
    }

    //Отрисовка комнаты с размерами width и height вокруг координат (x;y)
    public void render(int x, int y, int width, int height, Camera camera) {
        background.render(x, y, width, height, camera);
        mapControl.render(x, y, width, height);
    }

    public int objCount() {
        int count = 0;
        for (GameObject gameObject : objects)
            if (gameObject != null)
                count++;

        return count;
    }

    public GameObject getObject(int id) {
        return objects.get(id);
    }

    public int getObjectsVectorSize() {
        return objects.size();
    }

    public Vector<GameObject> getObjects() {
        return new Vector<>(objects);
    }

    //Добавление объекта в комнату
    public void objAdd(GameObject gameObject) {
        gameObject.setLocation(location);
        gameObject.getComponent(Position.class).id = objects.size();
        objects.add(gameObject);
        mapControl.add(gameObject);
    }

    //Удаление объекта из комнаты по id
    public void objDel(int id) {
        mapControl.del(id);//Используется objects, так что должно быть раньше
        objects.set(id, null);
    }

    //Удаление всех объектов в комнате
    public void destroy() {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) != null) objects.get(i).destroy();
        }
    }
}
