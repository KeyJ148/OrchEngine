package cc.abro.orchengine.resources.audios;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Loader;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.resources.JsonContainerLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioStorage {

    private static final Logger log = LogManager.getLogger(AudioStorage.class);

    private static final String CONFIG_PATH = "res/configs/audio.json";

    private Map<String, Audio> audioByName = new HashMap<>();

    public AudioStorage() {
        try {
            Map<String, String> audioNameToPath = JsonContainerLoader.loadInternalFile(Map.class, CONFIG_PATH);

            for (Map.Entry<String, String> entry : audioNameToPath.entrySet()) {
                String name = entry.getKey();
                String path = entry.getValue();
                if (audioByName.containsKey(name)) {
                    log.error("Audio \"" + name + "\" already exists");
                    Loader.exit();
                }

                audioByName.put(name, AudioLoader.getAudio(path));
            }
        } catch (IOException e) {
            log.error("Error loading audios", e);
            Loader.exit();
        }
    }

    public Audio getAudio(String name) {
        if (!audioByName.containsKey(name)) {
            log.error("Audio \"" + name + "\" not found");
            return null;
        }

        return audioByName.get(name);
    }
}
