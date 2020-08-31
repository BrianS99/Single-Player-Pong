package com.main;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Ball {

    // initializing ball variables
    public static final int SIZE = 22;

    private int x, y;
    private double xVel, yVel;
    private int speed = 6;

    private boolean changedDir;

    public Ball(){
        reset();

    }

    // resetting position and direction of the ball
    private void reset() {
        // init position
        x = Game.WIDTH / 2 - SIZE / 2;
        y = Game.HEIGHT / 2 - SIZE / 2;

        //init velocities
        xVel = Game.getSuitableXVel(ThreadLocalRandom.current().nextDouble(-0, 0.8));
        yVel = Game.posOrNeg(Math.sqrt(2 - (xVel*xVel)));

        changedDir = false;
    }

    // changes Y velocity
    public void changeYDir(){
        yVel *= -1;
    }

    // changes X velocity
    public void changeXDir(){

        // prohibits the ball to bounce back and forth in the X direction on the paddle
        if(changedDir == false) {
            xVel *= -1;
            changedDir = true;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x ,y, SIZE, SIZE);
    }

    // deals with all the ball movements and collisions
    public void update(Paddle p1, Paddle p2){
        // update movement
        x += xVel * speed;
        y += yVel * speed;

        // fixes a bug where sometimes the ball gets stuck in the pallet and bounces into the goal
        // now the ball cannot repeatedly bounce on the paddle
        if(x > 100 && x < 900){
            changedDir = false;
        }

        //collisions
        if(y + SIZE >= Game.HEIGHT || y <= 0){
            changeYDir();
        }

        // with walls
        if(x + SIZE >= Game.WIDTH){
            p1.addPoint();
            reset();
        }

        if(x <= 0){
            p2.addPoint();
            reset();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getXVel() {return xVel;}

    public double getYVel() {return yVel;}
}
