import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LevelCreator extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = Board.SCREEN_WIDTH, SCREEN_HEIGHT = Board.SCREEN_HEIGHT;
    private JButton save, undo;
    private JLabel levelNumberDisplay;
    private int startSquareX, startSquareY, releaseSquareX, releaseSquareY, xLength, yLength;

    private final int[][] newLevel = new int[18][4];
    private final JLabel[] drawnCars = new JLabel[18];
    private int numberOfCars = 0;

    private final IO io;

    public LevelCreator(IO io) {
        this.io = io;

        JFrame frame = new JFrame();
        frame.setTitle("Level Creator");
        frame.setLayout(null);
        frame.setSize(new Dimension(360, 440));
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        frame.add(this);

        this.setLayout(null);
        this.setSize(new Dimension(360, 440));
        this.setFocusable(true);
        this.setVisible(true);

        addButtons();

        this.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                startSquareX = e.getX() / 60;
                startSquareY = e.getY() / 60;
                System.out.println(startSquareX + "/" + startSquareY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                releaseSquareX = e.getX() / 60;
                releaseSquareY = e.getY() / 60;
                System.out.println(releaseSquareX + "/" + releaseSquareY);

                xLength = Math.abs(releaseSquareX - startSquareX) + 1;
                yLength = Math.abs(releaseSquareY - startSquareY) + 1;

                int xPos = Math.min(startSquareX, releaseSquareX);
                int yPos = Math.min(startSquareY, releaseSquareY);
                int length = xLength + yLength - 1;
                int orientation;
                if (xLength > yLength)
                    orientation = 0;
                else
                    orientation = 1;

                if (carIsInFrame() && (xLength == 1 ^ yLength == 1) && (xLength + yLength - 1 == 3 || xLength + yLength - 1 == 2)) {
                    System.out.println("car...");
                    addNewCar(xPos, yPos, length, orientation);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
    }

    private void addNewCar(int xPos, int yPos, int length, int orientation) {
        newLevel[numberOfCars][0] = xPos;
        newLevel[numberOfCars][1] = yPos;
        newLevel[numberOfCars][2] = length;
        newLevel[numberOfCars][3] = orientation;

        drawnCars[numberOfCars] = new JLabel();
        drawnCars[numberOfCars].setBounds(xPos * Board.UNIT_SIZE + 2, yPos * Board.UNIT_SIZE, xLength * Board.UNIT_SIZE, yLength * Board.UNIT_SIZE);
        addImage(drawnCars[numberOfCars], length, orientation);
        this.add(drawnCars[numberOfCars]);
        this.repaint();

        numberOfCars++;
    }

    private void removeLastCar() {
        numberOfCars--;
        newLevel[numberOfCars][0] = 0;
        newLevel[numberOfCars][1] = 0;
        newLevel[numberOfCars][2] = 0;
        newLevel[numberOfCars][3] = 0;

        drawnCars[numberOfCars] = null;
        this.remove(drawnCars[numberOfCars]);
    }

    private void createNewLevel() {
        io.saveLevel(newLevel);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (undo.equals(source)) {
            removeLastCar();
        } else if (save.equals(source)) {
            createNewLevel();
        }
    }

    public boolean carIsInFrame() {
        return !(releaseSquareX > 5 || releaseSquareX < 0 || releaseSquareY > 5 || releaseSquareY < 0);
    }

    private void addImage(JLabel label, int length, int direction) {
        if (length == 3) {
            if (direction == 0) {
                label.setIcon(new ImageIcon("images/purple_car_long_h.png"));
            } else
                label.setIcon(new ImageIcon("images/purple_car_long_v.png"));
        }
        if (length == 2) {
            if (direction == 0)
                label.setIcon(new ImageIcon("images/orange_car_short_h.png"));
            else
                label.setIcon(new ImageIcon("images/orange_car_short_v.png"));
        }
        label.revalidate();
    }

    private void addButtons() {

        undo = new JButton("undo");
        save = new JButton("save");
        levelNumberDisplay = new JLabel("-");

        undo.addActionListener(this);
        save.addActionListener(this);

        undo.setBounds(SCREEN_WIDTH / 4 * 3, SCREEN_HEIGHT - 45, SCREEN_WIDTH / 4, 45);
        save.setBounds(0, SCREEN_HEIGHT - 45, SCREEN_WIDTH / 4, 45);
        levelNumberDisplay.setBounds(SCREEN_WIDTH / 4, SCREEN_HEIGHT - 45, SCREEN_WIDTH / 2, 45);

        this.add(undo);
        this.add(save);
        this.add(levelNumberDisplay);

        levelNumberDisplay.setText("---");
        levelNumberDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        levelNumberDisplay.setHorizontalTextPosition(SwingConstants.CENTER);
    }

    public void paintComponent(Graphics g) {
        for (int i = 0; i < 7; i++) {
            g.drawLine(i * Board.UNIT_SIZE, 0, i * Board.UNIT_SIZE, 6 * Board.UNIT_SIZE);
            g.drawLine(0, i * Board.UNIT_SIZE, 6 * Board.UNIT_SIZE, i * Board.UNIT_SIZE);

        }
    }
}
