package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class IconReader
{
    private static IconReader ourInstance = new IconReader();

    public static IconReader getInstance()
    {
        return ourInstance;
    }

    public Image getWindowIcon()
    {
        Image img = null;

        String iconToGet = findPathFor(IconTypes.MINE);

        try
        {
            img = ImageIO.read(getClass().getResource(iconToGet));
        }
        catch (IOException e)
        {
            System.err.println("Cannot find icon file!");
            System.exit(-1);
        }

        return img;
    }

    public ImageIcon getIcon(IconTypes iconType)
    {
        Image img = null;

        String iconToGet = findPathFor(iconType);

        try
        {
            img = ImageIO.read(getClass().getResource(iconToGet));
        }
        catch (IOException e)
        {
            System.err.println("Cannot find icon file!");
            System.exit(-1);
        }

        return new ImageIcon(img);
    }

    public ImageIcon getIcon(int iconType)
    {
        switch (iconType)
        {
            case 1:
                return getIcon(IconTypes.ONE);
            case 2:
                return getIcon(IconTypes.TWO);
            case 3:
                return getIcon(IconTypes.THREE);
            case 4:
                return getIcon(IconTypes.FOUR);
            case 5:
                return getIcon(IconTypes.FIVE);
            case 6:
                return getIcon(IconTypes.SIX);
            case 7:
                return getIcon(IconTypes.SEVEN);
            case 8:
                return getIcon(IconTypes.EIGHT);
            default:
                return null;
        }
    }

    private String findPathFor(IconTypes iconType)
    {
        String iconToGet;

        switch (iconType)
        {
            case MINE:
                iconToGet = "Images/mine.png";
                break;
            case ONE:
                iconToGet = "Images/one.png";
                break;
            case TWO:
                iconToGet = "Images/two.png";
                break;
            case THREE:
                iconToGet = "Images/three.png";
                break;
            case FOUR:
                iconToGet = "Images/four.png";
                break;
            case FIVE:
                iconToGet = "Images/five.png";
                break;
            case SIX:
                iconToGet = "Images/six.png";
                break;
            case SEVEN:
                iconToGet = "Images/seven.png";
                break;
            case EIGHT:
                iconToGet = "Images/eight.png";
                break;
            case MARKED:
                iconToGet = "Images/marked.png";
                break;
            default:
                iconToGet = "";
        }

        return iconToGet;
    }

    protected enum IconTypes
    {
        MINE, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, MARKED
    }

    private IconReader()
    {
    }
}
