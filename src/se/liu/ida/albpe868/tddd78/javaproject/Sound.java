package se.liu.ida.albpe868.tddd78.javaproject;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Creates a new Thread for playing sounds while game is still running.
 */
public final class Sound {
    private final static String SOUNDFOLDER = "src/sounds/";
    private final static String TAKENHIT = SOUNDFOLDER + "takenhit1.wav";
    private final static String EXPLOSION = SOUNDFOLDER + "explosion.wav";
    private final static String BLASTER = SOUNDFOLDER + "blaster.wav";
    private final static String ENEMYBLASTER = SOUNDFOLDER + "enemyBlaster.wav";
    private final static String YOULOST = SOUNDFOLDER + "youLost.wav";
    private final static String INDESTRUCTIBLE = SOUNDFOLDER + "indestructible_new.wav";
    private final static String DOUBLEFIRERATE = SOUNDFOLDER + "double_firerate.wav";
    private final static String DOUBLESPEED = SOUNDFOLDER + "double_speed.wav";
    private final static String EXTRAHEALTH = SOUNDFOLDER + "extra_health.wav";

    private Sound() {
    }

    public static synchronized void play(final String soundToPlay, final boolean soundEnabled) {
        if (soundEnabled) {
            new Thread(() -> { // to make the sound run alongside the game's thread
                try {
                    Clip sound = AudioSystem.getClip();
                    // "The Clip plays back on a separate thread so you can't use try-with-resources."
                    // http://stackoverflow.com/questions/25564980/java-use-a-clip-and-a-try-with-resources-block-which-results-with-no-sound
                    AudioInputStream input = AudioSystem.getAudioInputStream(new File(soundToPlay));
                    sound.open(input);
                    sound.start();
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    LogHandler.log(Level.SEVERE, "Sound.play error: ", ex);
                }
            }).start();
        }
    }


    public static void playSoundTakenHit(boolean soundEnabled) {
        play(TAKENHIT, soundEnabled);
    }

    public static void playSoundExplosion(boolean soundEnabled) {
        Sound.play(EXPLOSION, soundEnabled);
    }

    public static void playSoundBlaster(boolean soundEnabled) {
        Sound.play(BLASTER, soundEnabled);
    }

    public static void playSoundEnemyBlaster(boolean soundEnabled) {
        Sound.play(ENEMYBLASTER, soundEnabled);
    }

    public static void playSoundIndestructible(boolean soundEnabled) {
        Sound.play(INDESTRUCTIBLE, soundEnabled);
    }

    public static void playSoundDoubleFirerate(boolean soundEnabled) {
        Sound.play(DOUBLEFIRERATE, soundEnabled);
    }

    public static void playSoundDoubleSpeed(boolean soundEnabled) {
        Sound.play(DOUBLESPEED, soundEnabled);
    }

    public static void playSoundExtraHealth(boolean soundEnabled) {
        Sound.play(EXTRAHEALTH, soundEnabled);
    }

    public static void playSoundYouLost(boolean soundEnabled) {
        Sound.play(YOULOST, soundEnabled);
    }
}
