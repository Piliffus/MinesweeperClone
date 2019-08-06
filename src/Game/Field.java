package Game;

import javax.swing.*;

public class Field extends JToggleButton
{
    private boolean isMine;
    private boolean isMarked;
    private Icon icon;
    private int borderingMines;
    private GameArea.Coordinate coordinates;

    public void clicked()
    {
        this.setEnabled(false);
        if (icon != null)
        {
            // we have to do this this way, because for some reason setDisabledIcon doesnt work if there is no setIcon
            this.setIcon(icon);
            this.setDisabledIcon(icon);
        }
    }

    public void reveal()
    {
        if (!isSelected())
        {
            if (isMine)
            {
                if (isMarked)
                {
                    rightClicked(null);
                }
                clicked();
            }
        }
    }

    public void rightClicked(GameArea gameArea)
    {
        if (!isSelected()) // clicked fields cannot be marked
        {
            if (!isMarked)
            {
                ImageIcon mark = IconReader.getInstance().getIcon(IconReader.IconTypes.MARKED);
                this.setEnabled(false);
                this.setIcon(mark);
                this.setDisabledIcon(mark);
                isMarked = true;
                if (gameArea != null)
                {
                    gameArea.increaseMarkAmount();
                }
            }
            else // isMarked
            {
                this.setEnabled(true);
                this.setIcon(null);
                this.setDisabledIcon(null);
                isMarked = false;
                if (gameArea != null)
                {
                    gameArea.decreaseMarkAmount();
                }
            }
        }
    }

    public int getBorderingMines()
    {
        return borderingMines;
    }

    public boolean isMine()
    {
        return isMine;
    }

    public GameArea.Coordinate getCoordinates()
    {
        return coordinates;
    }

    public Field(boolean isMine, int borderingMines, GameArea.Coordinate coordinates)
    {
        super();
        this.isMine = isMine;
        this.isMarked = false;
        this.icon = null;
        this.borderingMines = borderingMines;
        this.coordinates = coordinates;

        if (this.isMine)
        {
            this.icon = IconReader.getInstance().getIcon(IconReader.IconTypes.MINE);
        }
        else if (borderingMines >= 1)
        {
            this.icon = IconReader.getInstance().getIcon(borderingMines);
        }
    }
}
