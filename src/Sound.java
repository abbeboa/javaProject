import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Creates a new Thread for playing sounds while game is still running.
 */
public final class Sound
{
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

    public static synchronized void play(final String soundToPlay) {
	if (GamePanel.isSoundEnabled()) {
	    new Thread(() -> { // to make the sound run alongside the game's thread
		try {
		    Clip sound = AudioSystem.getClip();
		    AudioInputStream input = AudioSystem.getAudioInputStream(new File(soundToPlay));
		    sound.open(input);
		    sound.start();
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
		    e.printStackTrace();
		}
	    }).start();
	}
    }

    public static void playSoundTakenHit() {
	play(TAKENHIT);
    }

    public static void playSoundExplosion() {
	Sound.play(EXPLOSION);
    }

    public static void playSoundBlaster() {
	Sound.play(BLASTER);
    }

    public static void playSoundEnemyBlaster() {
	Sound.play(ENEMYBLASTER);
    }

    public static void playSoundIndestructible() {
	Sound.play(INDESTRUCTIBLE);
    }

    public static void playSoundDoubleFirerate() {
	Sound.play(DOUBLEFIRERATE);
    }

    public static void playSoundDoubleSpeed() {
	Sound.play(DOUBLESPEED);
    }

    public static void playSoundExtraHealth() {
	Sound.play(EXTRAHEALTH);
    }

    public static void playSoundYouLost() {
	Sound.play(YOULOST);
    }
}