package com.main;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Pause {
    private int x;
    private int x1;
    private int y;
    private int y1;
    private int xImage;
    private int yImage;
    private String text;
    private boolean firstTime;
    private String instructions;

    // initializing all the variables
    public Pause(){
         this.text = "WELCOME  TO PONG!";
         this.instructions = "Press SPACE   to Start/Pause";
         this.x = 220;
         this.x1 = 150;
         this.y1 = 300;
         this.y = 200;
         this.xImage = 100;
         this.yImage = 400;
         this.firstTime = true;
    }

    // draws the pause text, starting instructions
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString(this.text, x,y);
        g.drawString(this.instructions, x1,y1);

        Image image = Toolkit.getDefaultToolkit().getImage("Instructions.jpg");
        g.drawImage(image, xImage, yImage, 200, 150, new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        });
    }

    // determines what and where to display depending on if the user has started/paused the game
    public void update(boolean gameStarted){
        if(gameStarted){
            this.text = " PAUSED";
            this.instructions = "";
            this.firstTime = false;
            x = 1000;
            y = 1000;
            xImage = 1000;
            yImage = 1000;
        } else {
            if(!firstTime){
                x = 380;
                y = 1000*9/32;
                xImage = 100;
                yImage = 400;
            }
        }
    }
}
