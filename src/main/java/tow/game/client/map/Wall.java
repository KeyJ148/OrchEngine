package tow.game.client.map;

import tow.engine.Global;
import tow.engine.logger.Logger;
import tow.engine.image.TextureHandler;
import tow.engine.obj.components.Collision;

public class Wall extends MapObject {

    public int stabillity;

    public Wall(double x, double y, double direction, TextureHandler textureHandler, int mid){
        super(x, y, direction, textureHandler, mid);

        collision = new Collision(this, textureHandler.mask);
        stabillity = getStabillityByType(textureHandler.type);
    }

    private int getStabillityByType(String type){
        switch (type){
            case "home": return 100;
            case "tree": return 30;
            default:
                Global.logger.println("This type not have stability parameter: " + type, Logger.Type.ERROR);
                return 100;
        }
    }

    public void destroyByArmor(){
        destroy();
    }
}