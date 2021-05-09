package cc.abro.orchengine.resources.masks;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.Vector2;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.resources.JsonContainerLoader;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MaskLoader {

    public static Mask getMask(String path) {
        try {
            Mask mask = new Mask(loadMaskPointsFromFile(path));

            log.debug("Load mask \"" + path + "\" completed");
            return mask;
        } catch (Exception e) {
            log.error("Mask \"" + path + "\" not loading");
            return null;
        }
    }

    public static Mask createDefaultMask(int width, int height) {
        List<Vector2<Integer>> maskPoints = new ArrayList<>();
        maskPoints.add(new Vector2<>(0, 0));
        maskPoints.add(new Vector2<>(width - 1, 0));
        maskPoints.add(new Vector2<>(width - 1, height - 1));
        maskPoints.add(new Vector2<>(0, height - 1));

        return new Mask(maskPoints);
    }

    private static List<Vector2<Integer>> loadMaskPointsFromFile(String path) throws IOException {
        MaskPoint[] maskPoints = JsonContainerLoader.loadInternalFile(MaskPoint[].class, path);

        List<Vector2<Integer>> maskPointsList = new ArrayList<>();
        for (MaskPoint maskPoint : maskPoints) {
            maskPointsList.add(new Vector2<>(maskPoint.x, maskPoint.y));
        }

        return maskPointsList;
    }

    private static class MaskPoint {
        int x, y;
    }
}
