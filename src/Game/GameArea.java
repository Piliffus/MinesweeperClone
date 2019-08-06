package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.swing.JOptionPane.*;

public class GameArea extends JFrame implements ActionListener
{
    private int xSize;
    private int ySize;
    private Field[][] fields;
    private JPanel mineFieldDisplay;
    private JPanel menu;
    private JPanel mainDisplay;
    private int howManyMines;
    private int howManyMarks;
    private int windowXSize;
    private int windowYSize;
    private JLabel marksInfo;
    private List<Coordinate> minesCoordinates;

    private void initUI()
    {
        makePanel();
        makeButtons();
        makeMenuButtons();
        makeWindow();
        mainDisplay.updateUI();
    }

    private void makeMenuButtons()
    {
        Restart restart = new Restart();
        JButton restartButton = new JButton("Restart");

        howManyMarks = 0;
        String marksInfoText = howManyMarks + " / " + howManyMines;
        marksInfo = new JLabel(marksInfoText, IconReader.getInstance().getIcon(IconReader.IconTypes.MINE),
                SwingConstants.CENTER);

        Difficulty difficulty = new Difficulty();
        JButton difficultyButton = new JButton("Difficulty");

        Resolution resolution = new Resolution();
        JButton resolutionButton = new JButton("Window size");

        menu.add(restartButton);
        menu.add(difficultyButton);
        menu.add(resolutionButton);
        menu.add(marksInfo);

        restartButton.addActionListener(restart);
        difficultyButton.addActionListener(difficulty);
        resolutionButton.addActionListener(resolution);
    }

    private class Resolution implements ActionListener
    {
        // Resolution change button pressed
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            Object[] options = {"600x600",
                    "800x800",
                    "1000x1000"};
            int choice = JOptionPane.showOptionDialog(GameArea.this,
                    "Choose window size",
                    "Window size",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);

            switch (choice)
            {
                case YES_OPTION:
                    windowXSize = 600;
                    windowYSize = 600;
                    menu.setMaximumSize(new Dimension(windowXSize, 50));
                    GameArea.this.setSize(windowXSize, windowYSize);
                    mainDisplay.updateUI();
                    return;
                case NO_OPTION:
                    windowXSize = 800;
                    windowYSize = 800;
                    menu.setMaximumSize(new Dimension(windowXSize, 50));
                    GameArea.this.setSize(windowXSize, windowYSize);
                    mainDisplay.updateUI();
                    return;
                case CANCEL_OPTION:
                    windowXSize = 1000;
                    windowYSize = 1000;
                    menu.setMaximumSize(new Dimension(windowXSize, 50));
                    GameArea.this.setSize(windowXSize, windowYSize);
                    mainDisplay.updateUI();
                    return;
                default:
            }
        }
    }

    private class Difficulty implements ActionListener
    {
        // Difficulty change button pressed
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            Object[] options = {"Easy (8x8)",
                    "Medium (12x12)",
                    "Hard (16x16)"};
            int choice = JOptionPane.showOptionDialog(GameArea.this,
                    "Choose difficulty level",
                    "Difficulty",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);

            switch (choice)
            {
                case YES_OPTION:
                    xSize = 8;
                    ySize = 8;
                    restart();
                    return;
                case NO_OPTION:
                    xSize = 12;
                    ySize = 12;
                    restart();
                    return;
                case CANCEL_OPTION:
                    xSize = 16;
                    ySize = 16;
                    restart();
                    return;
                default:
            }
        }
    }

    public void increaseMarkAmount()
    {
        howManyMarks++;
    }

    public void decreaseMarkAmount()
    {
        howManyMarks--;
    }

    private class Marker implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent mouseEvent)
        {
            if (SwingUtilities.isRightMouseButton(mouseEvent))
            {
                if (mouseEvent.getSource().getClass() == Field.class)
                {
                    Field pressed = (Field) mouseEvent.getSource();
                    pressed.rightClicked(GameArea.this);
                    marksInfo.setText(howManyMarks + " / " + howManyMines);
                }
            }
        }

        // Other types of clicks don`t bother us
        @Override
        public void mousePressed(MouseEvent mouseEvent) {}

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {}

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {}

        @Override
        public void mouseExited(MouseEvent mouseEvent) {}
    }

    private class Restart implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int choice = popupRestartConfirmation();
            if (choice == YES_OPTION) restart();
        }

        private int popupRestartConfirmation()
        {
            return JOptionPane.showConfirmDialog(
                    GameArea.this,
                    "Are you sure you want to restart?",
                    "Restart",
                    JOptionPane.YES_NO_OPTION);
        }
    }

    private void exit()
    {
        dispose();
    }

    private void restart()
    {
        mainDisplay.remove(menu);
        mainDisplay.remove(mineFieldDisplay);
        remove(mainDisplay);
        fields = new Field[xSize][ySize];
        initUI();
    }

    private void makePanel()
    {
        mineFieldDisplay = new JPanel();
        mineFieldDisplay.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mineFieldDisplay.setLayout(new GridLayout(ySize, xSize, 0, 0));

        menu = new JPanel();
        menu.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        menu.setLayout(new GridLayout(1, 4, 50, 0));
        menu.setMaximumSize(new Dimension(windowXSize, 50));

        mainDisplay = new JPanel();
        mainDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainDisplay.setLayout(new BoxLayout(mainDisplay, BoxLayout.Y_AXIS));
    }

    private void makeButtons()
    {
        howManyMines = (int) ((xSize * ySize) / 6.4);
        Marker marker = new Marker();

        minesCoordinates = prepareMineCoordinates(howManyMines);

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                Coordinate nextField = new Coordinate(x, y);
                fields[nextField.x][nextField.y] = new Field(minesCoordinates.contains(nextField),
                        countBorderingMines(nextField, minesCoordinates), nextField);
                fields[nextField.x][nextField.y].addActionListener(this);
                fields[nextField.x][nextField.y].addMouseListener(marker);
                mineFieldDisplay.add(fields[nextField.x][nextField.y]);
            }
        }
    }

    private int countBorderingMines(Coordinate nextField, List<Coordinate> minesCoordinates)
    {
        int returnedValue = 0;
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                if ((x != 0) || (y != 0)) // don`t want to click itself
                {
                    int newX = nextField.x + x;
                    int newY = nextField.y + y;
                    if (coordinatesCorrect(newX, newY))
                    {
                        // check if mine here
                        if (minesCoordinates.contains(new Coordinate(newX, newY)))
                        {
                            returnedValue++;
                        }
                    }
                }
            }
        }

        return returnedValue;
    }

    private List<Coordinate> prepareMineCoordinates(int howManyMines)
    {
        List<Coordinate> returnedList = new ArrayList<>(howManyMines);
        Random random = new Random();
        for (int i = 0; i < howManyMines; i++)
        {
            Coordinate coordinate;
            do
            {
                int x = random.nextInt(xSize);
                int y = random.nextInt(ySize);
                coordinate = new Coordinate(x, y);
            }
            while (returnedList.contains(coordinate));

            returnedList.add(coordinate);
        }

        return returnedList;
    }

    // default left mouse button click
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if (actionEvent.getSource().getClass() == Field.class)
        {
            Field clickedField = (Field) actionEvent.getSource();
            // uncover and disable the field
            clickedField.clicked();

            // did player click on mine?
            if (clickedField.isMine())
            {
                revealMines();
                int choice = youLosePopup();
                if (choice == YES_OPTION)
                {
                    restart();
                }
                else
                {
                    exit();
                }
            }
            // if we clicked on field without bordering mines, we uncover it`s mineless neighbours
            else if (clickedField.getBorderingMines() == 0)
            {
                clickAllMinelessNeighbours(clickedField.getCoordinates());
            }

            // check if we won
            if (playerWon())
            {
                int choice = youWinPopup();
                if (choice == YES_OPTION)
                {
                    restart();
                }
                else
                {
                    exit();
                }
            }
        }
    }

    private void revealMines()
    {
        for (Coordinate mine : minesCoordinates)
        {
            fields[mine.x][mine.y].reveal();
        }
    }

    private boolean playerWon()
    {
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // if there is still at least one uncovered mineless field, we didnt win yet
                if (!fields[x][y].isSelected() && !fields[x][y].isMine())
                {
                    return false;
                }
            }
        }

        return true;
    }

    private int youWinPopup()
    {
        return JOptionPane.showConfirmDialog(
                GameArea.this,
                "You win! Do you want to play another game?",
                "Congratulations!",
                JOptionPane.YES_NO_OPTION);
    }

    private int youLosePopup()
    {
        return JOptionPane.showConfirmDialog(
                GameArea.this,
                "You lose! Do you want to restart?",
                "You lose!",
                JOptionPane.YES_NO_OPTION);
    }

    private void clickAllMinelessNeighbours(Coordinate coordinates)
    {
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                if ((x != 0) || (y != 0))
                {
                    int clickedX = coordinates.x + x;
                    int clickedY = coordinates.y + y;
                    if (coordinatesCorrect(clickedX, clickedY))
                    {
                        // only click fields without mines, the game would be really frustrating otherwise
                        if (!fields[clickedX][clickedY].isMine())
                        {
                            // don`t waste time clicking disabled buttons
                            if (fields[clickedX][clickedY].isEnabled())
                            {
                                fields[clickedX][clickedY].doClick();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean coordinatesCorrect(int checkedX, int checkedY)
    {
        return (checkedX >= 0) && (checkedX < xSize) && (checkedY >= 0) && (checkedY < ySize);
    }

    private void makeWindow()
    {
        mainDisplay.add(menu);
        mainDisplay.add(mineFieldDisplay);
        add(mainDisplay);
        setIconImage(IconReader.getInstance().getWindowIcon());
        setTitle("Minesweeper");
        setSize(windowXSize, windowYSize);
        setResizable(false); // we don`t want our elements to lose their shape
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public GameArea(int xSize, int ySize, int windowXSize, int windowYSize) throws HeadlessException
    {
        this.xSize = xSize;
        this.ySize = ySize;
        this.windowXSize = windowXSize;
        this.windowYSize = windowYSize;
        fields = new Field[xSize][ySize];
        initUI();
    }

    class Coordinate
    {
        int x;
        int y;

        @Override
        public boolean equals(Object other)
        {
            if (this == other) return true;

            else if (other.getClass() == Coordinate.class)
            {
                Coordinate otherC = (Coordinate) other;
                return (this.x == otherC.x) && (this.y == otherC.y);
            }

            else return false;
        }

        Coordinate(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
