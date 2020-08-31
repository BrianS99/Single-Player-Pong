/* Written by Brian Sun
    Date: August 18, 2020
    This is a Pong game I programmed, and it has an algorithm that allows it to play with a single player
    The player uses the W and S keys to control the paddle, and the objective is to score against the opponent by getting
    the ball into their side of the screen while defending your own goal
    The game has a start and pause screen, and you press the SPACE key to toggle between those.

 */

package com.main;

import javax.swing.*;

public class Pong {

    public Pong(String title, Game game){
        // setting up JFrame
        JFrame frame = new JFrame(title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();

    }
}
