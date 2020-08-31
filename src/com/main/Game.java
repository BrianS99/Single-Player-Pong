/* Written by Brian Sun
    Date: August 12, 2020
    This is a simple pong game, made following the tutorial from Coding Haven from YouTube.
 */

package com.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Canvas implements Runnable, KeyListener {

    private static final long serialVersionUID = -668240625892092764L;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = WIDTH * 9/16;

    public boolean running = false;
    private Thread gameThread;
    private boolean gameStarted;
    private boolean released;

    private Ball ball;
    private Paddle paddle1;
    private Paddle paddle2;
    private Pause pause;

    private boolean up1 = false;
    private boolean down1 = false;

    public Game(){
        canvasSetup();
        initialize();

        new Pong("Pong", this);

        this.addKeyListener(this);
        this.setFocusable(true);
    }

    public static double getSuitableXVel(double d) {
        if(d <= 0){
            return d - 0.6;
        }
        return d + 0.6;
    }

    // returns closest number that is within the range of the min and max values
    public static int ensureRange(int val, int min, int max) {

        return Math.min(Math.max(val, min), max);
    }

    // 50/50 picks positive or negative version of number
    public static double posOrNeg(double value) {
        double v = ThreadLocalRandom.current().nextDouble(-1, 1);
        if(v <= 0)
            return -value;
        else
            return value;
    }

    private void initialize() {
        ball = new Ball();
        this.gameStarted = false;
        this.released = true;

        paddle1 = new Paddle(Color.red, true);
        paddle2 = new Paddle(Color.cyan, false);

        pause = new Pause();
    }

    private void canvasSetup() {
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH*2,HEIGHT*2));
        this.setMinimumSize(new Dimension(WIDTH,HEIGHT));
    }

    @Override
    public void run() { // using a game loop

        this.requestFocus();
        
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                update();
                delta--;
                draw();
                frames++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }

        stop();

    }

    private void draw() {
        BufferStrategy buffer = this.getBufferStrategy();

        if(buffer == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffer.getDrawGraphics();

        drawBackground(g);

        ball.draw(g);

        paddle1.draw(g);
        paddle2.draw(g);

        pause.draw(g);

        g.dispose();
        buffer.show();
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH,HEIGHT);

        g.setColor(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(WIDTH / 2, 0, WIDTH/2, HEIGHT);

        Image image = Toolkit.getDefaultToolkit().getImage("Instructions.jpg");
        g.drawImage(image, 100, 1000, 200, 150, this);
    }

    private void update() {
        //update ball
        pause.update(gameStarted);

        if(gameStarted) {
            ball.update(paddle1, paddle2);

            //update paddles
            paddle1.update(ball);
            paddle2.update(ball);
        }
    }

    // starting the game thread
    public void start() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    public void stop() {
        try {
            gameThread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new Game();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W){
            paddle1.switchDirection(-1);
            up1 = true;
        }
        if(key == KeyEvent.VK_S){
            paddle1.switchDirection(1);
            down1 = true;
        }
        if(key == KeyEvent.VK_SPACE){
            if(gameStarted == false && released == true){
                gameStarted = true;
            } else if(gameStarted == true && released == true) {
                gameStarted = false;
            }
            released = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W){
            up1 = false;
        }
        if(key == KeyEvent.VK_S){
            down1 = false;
        }
        if(key == KeyEvent.VK_SPACE)
            released = true;

        if(!up1 && !down1)
            paddle1.stop();

    }
}
