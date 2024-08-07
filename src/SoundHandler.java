import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;



public class SoundHandler{

    public static void PlayMusic(String path){

        Clip clip;

        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(0);
        }
        catch (UnsupportedAudioFileException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
    }
}