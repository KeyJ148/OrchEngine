package cc.abro.orchengine.resources;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

@Log4j2
public class JsonContainerLoader {

    public static <T> T loadExternalFile(Class<T> containerClass, String path) throws IOException {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(path)) {
            return gson.fromJson(reader, containerClass);
        } catch (IOException e) {
            log.error("Unable to load external JSON file \"" + path + "\" to class " + containerClass.getName());
            throw e;
        }
    }

    public static <T> void saveExternalFile(T containerObject, String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new FileWriter(path)) {
            gson.toJson(containerObject, writer);
        } catch (IOException e) {
            log.error("Unable to save external JSON file \"" + path + "\" from class " + containerObject.getClass().getName());
            throw e;
        }
    }

    public static <T> T loadInternalFile(Class<T> containerClass, String path) throws IOException {
        Gson gson = new Gson();

        try (Reader reader = new InputStreamReader(ResourceLoader.getResourceAsStream(path))) {
            return gson.fromJson(reader, containerClass);
        } catch (IOException e) {
            log.error("Unable to load internal JSON file \"" + path + "\" to class " + containerClass.getName());
            throw e;
        }
    }
}
