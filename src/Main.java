import javax.swing.*;

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
        SoundHandler.PlayMusic("src/NotLikeUs.wav");

        BBL_Flappy flappy = new BBL_Flappy();
        frame.add(flappy);
        frame.pack();
        flappy.requestFocus();
        frame.setVisible(true);

    }
}