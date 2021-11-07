package cc.abro.orchengine.location;


import cc.abro.orchengine.gui.input.keyboard.KeyboardHandler;
import cc.abro.orchengine.gui.input.mouse.MouseHandler;
import cc.abro.orchengine.services.GuiService;

import java.util.HashSet;
import java.util.Set;

public class LocationManager {

    private final GuiService guiService;

    private Location activeLocation;
    private Set<Location> updatedLocations = new HashSet<>();

    public LocationManager(GuiService guiService) {
        this.guiService = guiService;
    }

    public Location getActiveLocation() {
        return activeLocation;
    }

    public void setActiveLocation(Location location) {
        setActiveLocation(location, true);
    }

    //Сделать комнату активной (update и render), одновременно может быть максимум одна активная комната
    public void setActiveLocation(Location location, boolean saveInput) {
        //Перенести нажатые клавиши и настройки мыши/курсора или нет
        GuiLocationFrame newFrame = location.getGuiLocationFrame();
        if (activeLocation != null && saveInput) {
            GuiLocationFrame oldFrame = activeLocation.getGuiLocationFrame();
            newFrame.keyboard = new KeyboardHandler(newFrame.getGuiFrame(), oldFrame.getKeyboard());
            newFrame.mouse = new MouseHandler(newFrame.getGuiFrame(), oldFrame.getMouse());
        } else {
            newFrame.keyboard = new KeyboardHandler(newFrame.getGuiFrame());
            newFrame.mouse = new MouseHandler(newFrame.getGuiFrame());
        }
        activeLocation = location;
        guiService.setFrameFocused(newFrame.getGuiFrame());
    }

    public Set<Location> getUpdatedLocations() {
        return new HashSet<>(updatedLocations);
    }

    public void setUpdatedLocations(Set<Location> locations) {
        updatedLocations = new HashSet<>(locations);
    }

    public void addUpdatedLocation(Location location) {
        updatedLocations.add(location);
    }

    public void removeUpdatedLocation(Location location) {
        updatedLocations.remove(location);
    }
}
