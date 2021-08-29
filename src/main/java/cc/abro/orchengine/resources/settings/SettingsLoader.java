package cc.abro.orchengine.resources.settings;

import cc.abro.orchengine.resources.JsonContainerLoader;

import java.io.File;
import java.io.IOException;

public class SettingsLoader {

    //TODO возможность указывать путь, для загрузки настроек с клиента, например. Ошибка при наличие двух конфигов с одним именем по одному пути
    private static final String PATH_EXTERNAL = "settings/";
    private static final String PATH_INTERNAL = "settings/";

    public static <T> T loadExternalSettings(Class<T> settingsContainerClass) throws IOException {
        String path = PATH_EXTERNAL + getSettingsFileName(settingsContainerClass);
        return JsonContainerLoader.loadExternalFile(settingsContainerClass, path);
    }

    public static <T> void saveExternalSettings(T settingsContainerObject) throws IOException {
        File externalFolder = new File(PATH_EXTERNAL);
        if (!externalFolder.exists()) externalFolder.mkdir();

        String path = PATH_EXTERNAL + getSettingsFileName(settingsContainerObject.getClass());
        JsonContainerLoader.saveExternalFile(settingsContainerObject, path);
    }

    public static <T> T loadInternalSettings(Class<T> settingsContainerClass) throws IOException {
        String path = PATH_INTERNAL + getSettingsFileName(settingsContainerClass);
        return JsonContainerLoader.loadInternalFile(settingsContainerClass, path);
    }

    private static String getSettingsFileName(Class settingsClass) {
        return settingsClass.getSimpleName().toLowerCase() + ".json";
    }
}
