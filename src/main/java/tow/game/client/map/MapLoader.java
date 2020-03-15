package tow.game.client.map;

import tow.engine.Global;
import tow.engine.logger.Logger;
import tow.engine.resources.JsonContainerLoader;

import java.util.HashMap;
import java.util.Map;

public class MapLoader {

    public static MapSpecification getMap(String path){
        try {
            MapContainer mapContainer = JsonContainerLoader.loadExternalFile(MapContainer.class, path);

            Map<Integer, MapObject> mapObjectById = new HashMap<>();
            for (int i = 0; i < mapContainer.mapObjectContainers.length; i++) {
                MapObjectContainer mapObjectContainer = mapContainer.mapObjectContainers[i];
                mapObjectById.put(i, new MapObject(i, mapObjectContainer.x, mapObjectContainer.y,
                        mapObjectContainer.direction, mapObjectContainer.type, mapObjectContainer.parameters));
            }

            MapSpecification mapSpecification = new MapSpecification(mapContainer.width, mapContainer.height, mapObjectById);
            Global.logger.println("Load map \"" + path + "\" completed", Logger.Type.DEBUG);
            return mapSpecification;
        } catch (Exception e){
            Global.logger.println("Audio \"" + path + "\" not loading", Logger.Type.ERROR);
            return null;
        }
    }

    private static class MapContainer{
        public int width, height;
        public MapObjectContainer[] mapObjectContainers;
    }

    private static class MapObjectContainer{
        public double x, y, direction;
        public String type;
        public Map<String, Object> parameters;
    }
}