package cc.abro.orchengine.resources.sprites;

import cc.abro.orchengine.context.EngineService;
import cc.abro.orchengine.exceptions.EngineException;
import cc.abro.orchengine.resources.JsonContainerLoader;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@EngineService
public class SpriteStorage {

    private static final String CONFIG_PATH = "configs/sprite.json";

    private Map<String, Sprite> spriteByName = new HashMap<>();

    public SpriteStorage() {
        try {
            SpriteContainer[] spriteContainers = JsonContainerLoader.loadInternalFile(SpriteContainer[].class, CONFIG_PATH);

            for (SpriteContainer spriteContainer : spriteContainers) {
                if (spriteByName.containsKey(spriteContainer.name)) {
                    log.error("Sprite \"" + spriteContainer.name + "\" already exists");
                    throw new IllegalStateException("Sprite \"" + spriteContainer.name + "\" already exists");
                }

                spriteByName.put(spriteContainer.name, SpriteLoader.getSprite(spriteContainer.texturePath, spriteContainer.maskPath));
            }
        } catch (IOException e) {
            log.error("Error loading sprites", e);
            throw new EngineException(e);
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
