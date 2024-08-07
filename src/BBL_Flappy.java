import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BBL_Flappy extends JPanel implements ActionListener, KeyListener{
        int boardWidth = 360;
        int boardHeight = 640;

        //Image files
        Image backgroundImage;
        Image bblImage;
        Image topPipeImage;
        Image bottomPipeImage;

        //Bird
        int birdX = boardWidth/8;
        int birdY = boardHeight/2;
        int birdWidth = 44;
        int birdHeight = 34;

        class Bird{
                int x = birdX;
                int y = birdY;
                int width = birdWidth;
                int height = birdHeight;
                Image img;

                Bird(Image img){
                        this.img = img;
                }
        }

        //Pipes
        int pipeX = boardWidth;
        int pipeY = 0;
        int pipeWidth = 64;
        int pipeHeight = 512;

        class Pipe{
                int x = pipeX;
                int y = pipeY;
                int width = pipeWidth;
                int height = pipeHeight;
                Image img;
                boolean passed = false;

                Pipe(Image img){
                        this.img = img;
                }
        }

        //game logic
        Bird bird;
        int velocityX = -4; //moves pipes left, simulates bird moving right
        int velocityY = 0;
        int gravity = 1;

        ArrayList<Pipe> pipes;
        Random random = new Random();
        Timer gameLoop;
        Timer placePipesTimer;

        //Timer musicTimer;
        boolean gameOver = false;
        double score = 0;

        BBL_Flappy(){
                setPreferredSize(new Dimension(boardWidth,boardHeight));
                //setBackground(Color.blue);
                setFocusable(true);
                addKeyListener(this);

                //load images
                backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
                bblImage = new ImageIcon(getClass().getResource("./BBLDRizzySprite.png")).getImage();
                topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
                bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

                //bird
                bird = new Bird(bblImage);

                //pipes
                pipes = new ArrayList<Pipe>();

                //place pipes timer
                placePipesTimer = new Timer(1500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                placePipes();
                        }
                });
                placePipesTimer.start();

                //game timer
                gameLoop = new Timer(1000/60,this);
                gameLoop.start();

                LoadMusic();
                playMusic("NotLikeUs");

        }

        public void placePipes(){
                //gives value between 0 and 1, multiplies by half of pipeHeight

                int randomPipeY = (int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
                int openingSpace = boardHeight/3;

                Pipe topPipe = new Pipe(topPipeImage);
                topPipe.y = randomPipeY;
                pipes.add(topPipe);

                Pipe bottomPipe = new Pipe(bottomPipeImage);
                bottomPipe.y = topPipe.y+pipeHeight+openingSpace;
                pipes.add(bottomPipe);
        }

        public void paintComponent(Graphics g){
                super.paintComponent(g);
                draw(g);
        }

        public void draw(Graphics g){
                //background
                g.drawImage(backgroundImage,0,0,boardWidth,boardHeight,null);

                //bird
                g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

                //pipes
                for (int i = 0; i<pipes.size(); i++){
                        Pipe pipe = pipes.get(i);
                        g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
                }

                //score
                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.PLAIN,32));
                if (gameOver){
                        g.drawString("Game Over: " + String.valueOf((int)score), 10, 35);
                }
                else{
                        g.drawString(String.valueOf((int)score),10,35);
                }
        }

        public void move() {
                //bird
                velocityY += gravity;
                bird.y += velocityY;
                bird.y = Math.max(bird.y,0);

                //pipes
                for (int i = 0; i<pipes.size(); i++){
                        Pipe pipe = pipes.get(i);
                        pipe.x += velocityX;

                        if (!pipe.passed && bird.x > pipe.x+pipe.width){
                                pipe.passed = true;
                                score += 0.5; //0.5 because there are 2 pipes being passed each time, 0.5*2 = 1
                        }

                        if (collision(bird, pipe)){
                                gameOver = true;
                                stopMusic("NotLikeUs");
                        }
                }

                if (bird.y>boardHeight){
                        gameOver = true;
                        stopMusic("NotLikeUs");
                }
        }

        public boolean collision(Bird a, Pipe b){
                return a.x<b.x + b.width && //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x && //a's top right corner passes b's top left corner
                a.y < b.y + b.height && //a's bottom left corner doesn't reach b's bottom left corner
                a.y +a.height >b.y; //a's bottom right corner doesn't reach b's top left corner
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                move();
                repaint();
                if(gameOver){
                        placePipesTimer.stop();
                        gameLoop.stop();
                }
        }

        @Override
        public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                        velocityY = -9;
                        if (gameOver){
                                //restart the game by resetting the conditions
                                bird.y = birdY;
                                velocityY = 0;
                                pipes.clear();
                                score = 0;
                                gameOver = false;
                                gameLoop.start();
                                placePipesTimer.start();
                                LoadMusic();
                                playMusic("NotLikeUs");
                        }
                }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        public HashMap<String, Clip> musicMap = new HashMap<String, Clip>();
        public void LoadMusic(){
                musicMap.put("NotLikeUs", generateClip("src/NotLikeUs.wav"));
        }

        public Clip generateClip(String soundPath){
                Clip clip = null;
                try {
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundPath).getAbsoluteFile());
                        clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                } catch (Exception e) {
                        System.out.println("Error loading sound: " + soundPath);
                        System.out.println(e);
                }
                return clip;
        }

        public void playMusic(String soundName) {
                // Play music from the sound name
                Clip clip = musicMap.get(soundName);
                clip.start();
        }

        public void stopMusic(String soundName) {
                // Stop music from the sound name
                Clip clip = musicMap.get(soundName);
                clip.stop();
        }
}