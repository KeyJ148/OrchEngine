package cc.abro.orchengine.resources.animations;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Loader;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.resources.JsonContainerLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnimationStorage {

    private static final Logger log = LogManager.getLogger(AnimationStorage.class);

    private static final String CONFIG_PATH = "res/configs/animation.json";

    private Map<String, Animation> animationByName = new HashMap<>();

    public AnimationStorage() {
        try {
            AnimationContainer[] animationContainers = JsonContainerLoader.loadInternalFile(AnimationContainer[].class, CONFIG_PATH);

            for (AnimationContainer animationContainer : animationContainers) {
                if (animationByName.containsKey(animationContainer.name)) {
                    log.error("Animation \"" + animationContainer.name + "\" already exists");
                    Loader.exit();
                }

                animationByName.put(animationContainer.name, AnimationLoader.getAnimation(animationContainer.texturePaths, animationContainer.maskPath));
            }
        } catch (IOException e) {
            log.error("Error loading animation", e);
            Loader.exit();
        }
    }

    public Animation getAnimation(String name) {
        if (!animationByName.containsKey(name)) {
            log.error("Animation \"" + name + "\" not found");
            return null;
        }

        return animationByName.get(name);
    }

    private static class AnimationContainer {
        String name;
        String[] texturePaths;
        String maskPath;
    }
}
