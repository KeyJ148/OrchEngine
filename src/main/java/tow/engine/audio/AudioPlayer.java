package tow.engine.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import tow.engine.resources.audios.Audio;
import tow.engine.resources.settings.SettingsStorage;
import tow.engine.image.Camera;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.openal.ALC10.*;

public class AudioPlayer {

    private List<AudioSource> audioSources = new LinkedList<>();
    private long context, device;

    public AudioPlayer(){
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

    public void playSoundEffect(Audio audio, int x, int y, int range) {
        /*
        Т.к. при приближение к границе экрана абсолютная позиция камеры фиксирцуется,
        вне зависимости от положения объекта за которым следует камера, то получается,
        что при приближение к границы экрана звук становится слышен тише, хотя источник
        звука находится на таком же расстояние от объекта танка (игрока) как и раньше
        Условие (Camera.getFollowObject() != null) исправляет этот баг
        */
        double listenerX = Camera.absoluteX;
        double listenerY = Camera.absoluteY;

        if (Camera.getFollowObject() != null){
            listenerX = Camera.getFollowObject().position.x;
            listenerY = Camera.getFollowObject().position.y;
        }

        double dis = Math.sqrt(Math.pow(x-listenerX, 2) + Math.pow(y-listenerY, 2));

        if (dis < range){
            float soundVolume = (float) (SettingsStorage.MUSIC.SOUND_VOLUME * (1-(dis/range)));
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
    private void deleteWasteAudioSources(){
        Iterator<AudioSource> it = audioSources.listIterator();
        while (it.hasNext()){
            AudioSource source = it.next();

            if (source.isStopped()){
                source.delete();
                it.remove();
            }
        }
    }

    public void close(){
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}