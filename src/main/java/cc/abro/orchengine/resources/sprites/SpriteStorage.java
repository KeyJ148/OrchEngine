package cc.abro.orchengine.resources.sprites;

import cc.abro.orchengine.Loader;
import cc.abro.orchengine.cycle.Render;
import cc.abro.orchengine.resources.JsonContainerLoader;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class SpriteStorage {

    private static final String CONFIG_PATH = "res/configs/sprite.json";

    private Map<String, Sprite> spriteByName = new HashMap<>();

    //TODO now render used for queue creation in Manager.services
    public SpriteStorage(Render render) {
        try {
            SpriteContainer[] spriteContainers = JsonContainerLoader.loadInternalFile(SpriteContainer[].class, CONFIG_PATH);

            for (SpriteContainer spriteContainer : spriteContainers) {
                if (spriteByName.containsKey(spriteContainer.name)) {
                    log.error("Sprite \"" + spriteContainer.name + "\" already exists");
                    Loader.exit();
                }

                spriteByName.put(spriteContainer.name, SpriteLoader.getSprite(spriteContainer.texturePath, spriteContainer.maskPath));
            }
        } catch (IOException e) {
            log.error("Error loading sprites", e);
            Loader.exit();
        }
    }

    public Sprite getSprite(String name) {
        if (!spriteByName.containsKey(name)) {
            log.error("Sprite \"" + name + "\" not found");
            return null;
        }

        return spriteByName.get(name);
    }

    private static class SpriteContainer {
        public String name;
        public String texturePath;
        public String maskPath;
    }
}
