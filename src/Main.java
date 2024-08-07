import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class Main {



    public static void main(String[] args) throws Exception{
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("BBL Flappy");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BBL_Flappy flappy = new BBL_Flappy();
        frame.add(flappy);
        frame.pack();
        flappy.requestFocus();
        frame.setVisible(true);

    }
}