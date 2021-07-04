import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {

    private JButton undo, createNewLevel, nextLevel, resetLevel;
    public boolean[][] occupiedTiles = new boolean[6][6];
    public Car[] cars;
    public static final int UNIT_SIZE = 57, SCREEN_WIDTH = UNIT_SIZE * 6, SCREEN_HEIGHT = UNIT_SIZE * 6 + 50;
    public static final Dimension dimension = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);

    private int currentLevelNumber = 0;

    IO io;

    public Board() {
        super(null);
        this.setPreferredSize(dimension);
        this.setFocusable(true);
        setVisible(true);
        initialize();
        addButtons();
    }

    private void initialize() {

        io = new IO();

        int [][] currentLevel = io.readLevel(currentLevelNumber);

        System.out.println(currentLevel.length);

        cars = new Car[currentLevel.length];

        for (int i = 0; i < occupiedTiles.length; i++) {
            for (int j = 0; j < occupiedTiles[0].length; j++) {
                occupiedTiles[i][j] = false;
            }
        }

        for (int i = 0; i < currentLevel.length; i++) {
            cars[i] = new Car(currentLevel[i][0], currentLevel[i][1], currentLevel[i][2],
                    currentLevel[i][3], i, this);
            cars[i].revalidate();
            this.add(cars[i]);
        }
    }

    public void finishGame() {
        System.out.println("COMPLETE");

        JOptionPane.showMessageDialog(null, "Complete!", "Traffic Jam", JOptionPane.INFORMATION_MESSAGE);

        deleteCars();
    }

    private void deleteCars() {
        for (int i = 0; i < cars.length; i++) {
            cars[i].setIcon(null);
            this.remove(cars[i]);
            cars[i] = null;
        }
        this.repaint();
        this.revalidate();

        nextLevel();
    }

    private void nextLevel() {
        currentLevelNumber++;
        initialize();
    }

    private void undoLastMove() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (undo.equals(source)) {
            undoLastMove();
        }else if (createNewLevel.equals(source)) {
            new LevelCreator(io);

        }else if (nextLevel.equals(source)) {
            deleteCars();

        }else if (resetLevel.equals(source)) {
            currentLevelNumber--;
            deleteCars();
        }
    }

    private void addButtons() {

        createNewLevel = new JButton("create");
        undo = new JButton("undo");
        resetLevel = new JButton("reset");
        nextLevel = new JButton("next");

        createNewLevel.addActionListener(this);
        nextLevel.addActionListener(this);
        resetLevel.addActionListener(this);
        undo.addActionListener(this);

        createNewLevel.setBounds(0,SCREEN_HEIGHT - 45, SCREEN_WIDTH / 4,45);
        undo.setBounds(SCREEN_WIDTH / 4 ,SCREEN_HEIGHT - 45, SCREEN_WIDTH / 4,45);
        resetLevel.setBounds(SCREEN_WIDTH / 4 * 2,SCREEN_HEIGHT - 45, SCREEN_WIDTH / 4,45);
        nextLevel.setBounds(SCREEN_WIDTH / 4 * 3,SCREEN_HEIGHT - 45, SCREEN_WIDTH / 4,45);

        this.add(resetLevel);
        this.add(undo);
        this.add(createNewLevel);
        this.add(nextLevel);

    }
}

