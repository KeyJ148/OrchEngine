package cc.abro.orchengine.resources;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class JsonContainerLoader {

    public static <T> T loadExternalFile(Class<T> containerClass, String path) throws IOException {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(path)) {
            return gson.fromJson(reader, containerClass);
        } catch (IOException e) {
            Global.logger.println("Unable to load external JSON file \"" + path + "\" to class " + containerClass.getName(), Logger.Type.ERROR);
            throw e;
        }
    }

    public static <T> void saveExternalFile(T containerObject, String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new FileWriter(path)) {
            gson.toJson(containerObject, writer);
        } catch (IOException e) {
            Global.logger.println("Unable to save external JSON file \"" + path + "\" from class " + containerObject.getClass().getName(), Logger.Type.ERROR);
            throw e;
        }
    }

    public static <T> T loadInternalFile(Class<T> containerClass, String path) throws IOException {
        Gson gson = new Gson();

        try (Reader reader = new InputStreamReader(ResourceLoader.getResourceAsStream(path))) {
            return gson.fromJson(reader, containerClass);
        } catch (IOException e) {
            Global.logger.println("Unable to load internal JSON file \"" + path + "\" to class " + containerClass.getName(), Logger.Type.ERROR);
            throw e;
        }
    }
}
