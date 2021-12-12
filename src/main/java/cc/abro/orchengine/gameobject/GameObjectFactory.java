package cc.abro.orchengine.gameobject;

import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.gameobject.components.render.SpriteRender;
import cc.abro.orchengine.resources.textures.Texture;

import java.util.Arrays;
import java.util.List;

public class GameObjectFactory {

    public static GameObject create() {
        return create(0, 0);
    }

    public static GameObject create(double x, double y) {
        return create(x, y, 0);
    }

    public static GameObject create(double x, double y, int depth) {
        return new GameObject(Arrays.asList(new Position(x, y, depth)));
    }

    public static GameObject create(double x, double y, int depth, double directionDraw) {
        GameObject gameObject = create(x, y, depth);
        gameObject.getComponent(Position.class).setDirectionDraw(directionDraw);

        return gameObject;
    }

    public static GameObject create(double x, double y, int depth, double directionDraw, Texture texture) {
        GameObject gameObject = create(x, y, depth, directionDraw);
        gameObject.setComponent(new SpriteRender(texture));

        return gameObject;
    }

    public static GameObject create(double x, double y, int depth, double directionDraw, List<Texture> textures) {
        GameObject gameObject = create(x, y, depth, directionDraw);
        gameObject.setComponent(new AnimationRender(textures));

        return gameObject;
    }

    public static GameObject create(QueueComponent component) {
        GameObject gameObject = create(0, 0);
        gameObject.setComponent(component);
        return gameObject;
    }
}
