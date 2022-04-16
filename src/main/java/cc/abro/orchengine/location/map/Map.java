package cc.abro.orchengine.location.map;

import cc.abro.orchengine.gameobject.GameObject;
import cc.abro.orchengine.location.Location;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//TODO переименовать класс
public class Map {

    public Background background = new Background(); //Фон карты (цвет и текстура)
    public MapControl mapControl; //Массив со всеми чанками и объектами
    private final Set<GameObject> objects = Collections.newSetFromMap(new ConcurrentHashMap<>()); //Массив со всеми объектами
    //TODO а не будет дедлока при попытке удалить объект из сета в процессе обхода сета при помощи итератора?

    private final Location location;//TODO del from this class

    public Map(int width, int height, Location location) {
        this.location = location;
        mapControl = new MapControl(width, height);
    }

    public void update(long delta) {
        //TODO не заменяю на foreach, т.к. с ним ошибка:
        /*java.util.ConcurrentModificationException: null
            at java.util.Vector$Itr.checkForComodification(Vector.java:1298) ~[?:?]
            at java.util.Vector$Itr.next(Vector.java:1254) ~[?:?]
            at cc.abro.orchengine.location.map.Map.update(Map.java:23) ~[OrchEngine-0.1.0.jar:?]*/

        for (GameObject gameObject : objects) {
            gameObject.update(delta);
        }
    }

    //Отрисовка комнаты с размерами width и height вокруг координат (x;y)
    public void render(int x, int y, int width, int height, Camera camera) {
        background.render(x, y, width, height, camera);
        mapControl.render(x, y, width, height);
    }

    public int getCountObjects() {
        int count = 0;
        for (GameObject gameObject : objects)
            if (gameObject != null)
                count++;

        return count;
    }

    public Set<GameObject> getObjects() {
        return objects;
    }

    //Добавление объекта в комнату
    public void add(GameObject gameObject) {
        if (gameObject.getLocation() != null){
            gameObject.getLocation().getMap().remove(gameObject);
        }

        gameObject.getLocationHolder().setLocation(location);
        objects.add(gameObject);
        mapControl.add(gameObject);
    }

    //Удаление объекта из комнаты
    public void remove(GameObject gameObject) {
        mapControl.del(gameObject); //Используется objects, так что должно быть раньше
        objects.remove(gameObject);
    }

    //Уничтожение всех объектов в комнате //TODO add "remove without destroy() all objects" method
    public void destroy() {
        for (GameObject gameObject : objects) {
            gameObject.destroy();
        }
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
