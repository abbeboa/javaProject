import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public final class Sound {

    private Sound() {}

    public static synchronized void play(final String soundToPlay) {
        new Thread(() -> { // to make the sound run alongside the game's thread
            try {
                Clip sound = AudioSystem.getClip();
                AudioInputStream input = AudioSystem.getAudioInputStream(new File(soundToPlay));
                sound.open(input);
                sound.start();
            } catch (Exception e) {
                System.out.println("play sound error: " + e.getMessage());
            }
        }
        ).start();
    }
}