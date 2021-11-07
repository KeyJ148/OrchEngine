package cc.abro.orchengine.audio;

import cc.abro.orchengine.Manager;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.map.Camera;
import cc.abro.orchengine.map.LocationManager;
import cc.abro.orchengine.resources.audios.Audio;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.picocontainer.Startable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.openal.ALC10.*;

public class AudioPlayer implements Startable{

    private final List<AudioSource> audioSources = new LinkedList<>();
    private long context, device;

    @Override
    public void start() {
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));
    }

    @Override
    public void stop() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public void playSoundEffect(Audio audio, int x, int y, int range) {
        /*
        Т.к. при приближение к границе экрана абсолютная позиция камеры фиксирцуется,
        вне зависимости от положения объекта за которым следует камера, то получается,
        что при приближение к границы экрана звук становится слышен тише, хотя источник
        звука находится на таком же расстояние от объекта танка (игрока) как и раньше
        Условие (Camera.getFollowObject() != null) исправляет этот баг
        */
        Camera camera = Manager.getService(LocationManager.class).getActiveLocation().camera;
        double listenerX = camera.getX();
        double listenerY = camera.getY();

        if (camera.getFollowObject() != null) {
            listenerX = camera.getFollowObject().getComponent(Position.class).x;
            listenerY = camera.getFollowObject().getComponent(Position.class).y;
        }

        double dis = Math.sqrt(Math.pow(x - listenerX, 2) + Math.pow(y - listenerY, 2));

        if (dis < range) {
            float soundVolume = (float) (SettingsStorage.MUSIC.SOUND_VOLUME * (1 - (dis / range)));
            playSoundEffect(audio, soundVolume);
        }
    }

    //Проигрывание с громкостью в диапазоне [0; 1]
    private void playSoundEffect(Audio audio, float volume) {
        deleteWasteAudioSources();

        AudioSource source = new AudioSource();
        source.setAudio(audio);
        source.setVolume(volume);
        source.play();
        audioSources.add(source);
    }

    //Удаление из списка и из памяти источников, которые закончили проигрывание звука
    private void deleteWasteAudioSources() {
        Iterator<AudioSource> it = audioSources.listIterator();
        while (it.hasNext()) {
            AudioSource source = it.next();

            if (source.isStopped()) {
                source.delete();
                it.remove();
            }
        }
    }
}
