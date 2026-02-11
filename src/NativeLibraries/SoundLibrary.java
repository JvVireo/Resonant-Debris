package NativeLibraries;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundLibrary {

    private static final Map<String, Clip> soundCache = new HashMap<>();

    public static void playAudio(String soundFilePath) {
        playAudio(soundFilePath, 1.0f,0,false);
    }
    public static void playAudio(String soundFilePath, boolean cycle) {
        playAudio(soundFilePath, 1.0f,0,cycle);
    }
    public static void playAudio(String soundFilePath, float volume, boolean cycle) {
        playAudio(soundFilePath, volume,0,cycle);
    }
    public static void playAudio(String soundFilePath, float volume) {
        playAudio(soundFilePath, volume,0,false);
    }
    public static void playAudio(String soundFilePath, int framePos){
        playAudio(soundFilePath,1.0f,framePos, false);
    }
    public static void playAudio(String soundFilePath, float volume, int framePos){
        playAudio(soundFilePath,volume,framePos, false);
    }
    public static void playAudio(String soundFilePath, float volume, int framePos, boolean cycle) {
        try {
            Clip clip = getClip(soundFilePath);
            if (clip != null) {

                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                if (volumeControl != null) {
                    float gain = 20f * (float) Math.log10(volume);
                    gain = Math.max(volumeControl.getMinimum(), Math.min(gain, volumeControl.getMaximum()));
                    volumeControl.setValue(gain);
                }
                clip.setFramePosition(framePos);
                if(cycle){
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Clip getClip(String soundFilePath) {
        if (soundCache.containsKey(soundFilePath)) {
            Clip clip = soundCache.get(soundFilePath);
            clip.setFramePosition(0);
            return clip;
        }

        try {
            File soundFile = new File(soundFilePath);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);

                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(audioStream);
                soundCache.put(soundFilePath, clip);
                return clip;
            } else {
                System.err.println("Sound file not found: " + soundFilePath);
                return null;
            }
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void stopAudio(String soundFilePath) {
        Clip clip = soundCache.get(soundFilePath);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }
}