package com.main;

import java.awt.*;

public class Paddle {
    private int x, y;
    private int locY;
    private int vel = 0;
    private int speed = 10;
    private int width = 18, height = 85;
    private int score = 0;
    private Color color;
    private boolean left;

    public Paddle(Color c, boolean left){
        // initializing the paddle variables
        color = c;

        this.left = left;

        if(left){
            x = 0;
        } else
            x = Game.WIDTH - width;
        y = Game.HEIGHT / 2 - height / 2;
    }

    public void addPoint(){
        score++;
    }

    // draws the paddles and the scores for each player
    public void draw(Graphics g) {
        g.setColor(color);
        g.fill3DRect(x, y, width, height,true);

        int sx;
        String scoreText = Integer.toString(score);
        Font font = new Font("Arial", Font.PLAIN, 50);

        int strWidth = g.getFontMetrics(font).stringWidth(scoreText) + 1;
        int padding = 25;

        if(left)
            sx = Game.WIDTH / 2 - padding - strWidth;
        else
            sx = Game.WIDTH / 2 + padding;

        g.setFont(font);
        g.drawString(scoreText, sx, 50);
    }

    // deals with moving the paddles, using key inputs for player and algorithm for computer
    public void update(Ball b) {
        //update position
        if(left) {
            y = Game.ensureRange(y += vel, 0, Game.HEIGHT - height);
        }

        int ballx = b.getX();
        int bally = b.getY();

        // when the ball is at the specified location, perform a calculation
        if(ballx >= 590 && ballx <= 600 && b.getXVel() > 0){
            locY = (int) getLocation(b.getY(),b.getXVel(),b.getYVel());
            System.out.println(locY);
        }

        // moves the paddle toward where the predicted collision will happen
        if(!left && ballx > 600 && b.getXVel() > 0) {
            if (y - locY > 8 || locY - y > 8) {
                if (y < locY) {
                    y += 8;
                } else if (y > locY) {
                    y -= 8;
                }
            }

        }

        //collisions with ball
        if(left){

            if(ballx <= 1.2*width && bally + 1.1*Ball.SIZE >= y -5 && bally <= y + height + 5)
                b.changeXDir();
        } else {

            if(ballx + Ball.SIZE >= Game.WIDTH - 1.2*width && bally + 1.1*Ball.SIZE >= y-5 && bally <= y + height + 5)
                b.changeXDir();
        }

    }

    public void switchDirection(int direction) {
        vel = speed * direction;
    }

    // this method is the algorithm that performs all the calculations based on the ball's instant measured velocity
    // and predicts where it will hit
    public double getLocation(int y, double xVel, double yVel){

        double HEIGHT = 1000.0*9/16;
        double location =  y + (380 * yVel / xVel);

        if(location<HEIGHT && location > 0)
            location -= 50;

        else if(location > HEIGHT && location < 1.9*HEIGHT) {
            System.out.println(xVel + " " + yVel + " " + y);
            System.out.println(location);
            location = location - (2 * (location - HEIGHT)) - 100;
            if(yVel >= 1.2){
                location -= 60;
            }
            System.out.println("single bounce");
        }

        else if(location >= 1.9*HEIGHT && location < 2.3*HEIGHT) {
            location = location - (2 * HEIGHT )+ 75;
            if(location > 200){
                location += 60;
            }
            System.out.println("double bounce");
        }
        else if(location >= 2.3*HEIGHT && location < 3*HEIGHT) {
            location = location - (2 * HEIGHT )+70;
            if(location > 200){
                location += 60;
            }
            System.out.println("big double bounce");
        }
        else if(location >= 3*HEIGHT)
            location = location - 2*HEIGHT - (2*(location - 2*HEIGHT)) -50;

        else if(location <= 0 && location > -HEIGHT) {
            System.out.println(location);
            location = location + (2 * (0 - location)) - 20;
            System.out.println("Single bounce");
        }

        else if(location <= -HEIGHT && location > -2*HEIGHT) {
            System.out.println(location);
            location = location + 2 * HEIGHT - 50;
            if(location < 275){
                location -= 80;
            }
            System.out.println("double bounce");
        }
        else if(location <= -2*HEIGHT)
            location = location + 2*HEIGHT + (2*((-2*HEIGHT) - location));

        if(location < 0)
            location = 0;
        else if(location > 480){
            location = 480;
        }
        return location;
    }

    public void stop(){
        vel = 0;
    }
}
