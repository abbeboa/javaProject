import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Creates a new Thread for playing sounds while game is still running.
 */
public final class Sound {

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
            }
            ).start();
        }
    }
}