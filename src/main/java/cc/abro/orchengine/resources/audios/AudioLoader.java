package cc.abro.orchengine.resources.audios;

import cc.abro.orchengine.Global;
import cc.abro.orchengine.gameobject.components.render.AnimationRender;
import cc.abro.orchengine.resources.ResourceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_memory;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class AudioLoader {

    private static final Logger log = LogManager.getLogger(AudioLoader.class);

    public static Audio getAudio(String path) {
        try (InputStream in = ResourceLoader.getResourceAsStream(path)) {
            IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
            IntBuffer sampleRateBuffer = BufferUtils.createIntBuffer(1);

            byte[] audioFileByteArray = in.readAllBytes();
            ByteBuffer audioFileByteBuffer = BufferUtils.createByteBuffer(audioFileByteArray.length);
            audioFileByteBuffer.put(audioFileByteArray);
            audioFileByteBuffer.flip();

            ShortBuffer audioFileRawBuffer = stb_vorbis_decode_memory(audioFileByteBuffer, channelsBuffer, sampleRateBuffer);

            int channels = channelsBuffer.get();
            int sampleRate = sampleRateBuffer.get();

            int format;
            switch (channels) {
                case 1:
                    format = AL_FORMAT_MONO16;
                    break;
                case 2:
                    format = AL_FORMAT_STEREO16;
                    break;
                default:
                    format = -1;
            }

            Audio audio = new Audio();
            alBufferData(audio.getID(), format, audioFileRawBuffer, sampleRate);
            free(audioFileRawBuffer);

            log.debug("Load audio \"" + path + "\" completed");
            return audio;
        } catch (Exception e) {
            log.error("Audio \"" + path + "\" not loading");
            return null;
        }
    }

}
