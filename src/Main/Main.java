package Main;

import Game.GameArea;

import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            GameArea ex = new GameArea(12, 12, 1000, 1000);
            ex.setVisible(true);
        });
    }
}
