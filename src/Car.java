import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Car extends JLabel implements ActionListener {

    private final int carNumber;
    private final Board board;
    private int xPos, yPos, xSize, ySize;
    private int xPosPx, yPosPx, xSizePx, ySizePx;

    private boolean mainCar = false;
    private int startXPos, startYPos, releaseXPos, releaseYPos, moveDistanceX, moveDistanceY;
    private char carDirection;

    public Car(int xPosition, int yPosition, int length, int direction, int carNumber, Board board) {
        this.carNumber = carNumber;
        this.board = board;
        xPosPx = xPosition * Board.UNIT_SIZE;
        yPosPx = yPosition * Board.UNIT_SIZE;
        xPos = xPosition;
        yPos = yPosition;

        if (direction == 0) {
            xSizePx = Board.UNIT_SIZE * length;
            ySizePx = Board.UNIT_SIZE;
            xSize = length;
            ySize = 1;
            carDirection = 'H';

            for (int i = 0; i < length; i++) {
                this.board.occupiedTiles[yPosition][xPosition + i] = true;
            }
        }
        if (direction == 1) {
            xSizePx = Board.UNIT_SIZE;
            ySizePx = Board.UNIT_SIZE * length;
            xSize = 1;
            ySize = length;

            carDirection = 'V';

            for (int i = 0; i < length; i++) {
                this.board.occupiedTiles[yPosition + i][xPosition] = true;
            }
        }

        this.setBounds(xPosPx, yPosPx, xSizePx, ySizePx);

        if (yPosition == 2 && length == 2 && direction == 0) {
            addMainCar();
            this.mainCar = true;
        } else
            addImage(length, direction);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                startXPos = e.getXOnScreen();
                startYPos = e.getYOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                releaseXPos = e.getXOnScreen();
                releaseYPos = e.getYOnScreen();

                moveDistanceX = startXPos - releaseXPos;
                moveDistanceY = startYPos - releaseYPos;

                if (Math.abs(moveDistanceY) > Math.abs(moveDistanceX)) {
                    if (moveDistanceY < 0)
                        move('D', Math.abs(moveDistanceY) / Board.UNIT_SIZE);
                    else {
                        move('U', Math.abs(moveDistanceY) / Board.UNIT_SIZE);
                    }

                } else if (Math.abs(moveDistanceY) < Math.abs(moveDistanceX)) {
                    if (moveDistanceX < 0)
                        move('R', Math.abs(moveDistanceX) / Board.UNIT_SIZE);
                    else
                        move('L', Math.abs(moveDistanceX) / Board.UNIT_SIZE);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        this.setVisible(true);
    }

    private void move(char direction, int intensity) {

        if (((direction == 'U' || direction == 'D') && carDirection == 'V') ||
                ((direction == 'R' || direction == 'L') && carDirection == 'H')) {

            for (int i = intensity; i >= 1; i--) {
                switch (direction) {
                    case 'U':
                        updateCarV(false);
                        if (!checkCollisions(direction))
                            yPos = yPos - 1;
                        else
                            i = 0;
                        updateCarV(true);
                        break;
                    case 'D':
                        updateCarV(false);
                        if (!checkCollisions(direction))
                            yPos = yPos + 1;
                        else
                            i = 0;
                        updateCarV(true);
                        break;
                    case 'L':
                        updateCarH(false);
                        if (!checkCollisions(direction))
                            xPos = xPos - 1;
                        else
                            i = 0;
                        updateCarH(true);
                        break;
                    case 'R':
                        updateCarH(false);
                        if (!checkCollisions(direction))
                            xPos = xPos + 1;
                        else
                            i = 0;
                        updateCarH(true);
                }
            }
            xPosPx = xPos * Board.UNIT_SIZE;
            yPosPx = yPos * Board.UNIT_SIZE;
        }
        this.setBounds(xPosPx, yPosPx, xSizePx, ySizePx);

        if (this.mainCar && xPos == 0) {
            board.finishGame();
        }
        revalidate();
    }

    private void updateCarH(boolean replacement) {
        for (int i = 0; i < xSize; i++)
            board.occupiedTiles[yPos][xPos + i] = replacement;
    }

    private void updateCarV(boolean replacement) {
        for (int i = 0; i < ySize; i++)
            board.occupiedTiles[yPos + i][xPos] = replacement;
    }

    private boolean checkCollisions(char direction) {
        if (checkBorderCollisions(direction))
            return true;
        else return checkCarCollisions(direction);
    }

    private boolean checkCarCollisions(char direction) {
        boolean newOccupiedTile;

        switch (direction) {
            case 'U' -> newOccupiedTile = board.occupiedTiles[yPos - 1][xPos];
            case 'D' -> newOccupiedTile = board.occupiedTiles[yPos + ySize][xPos];
            case 'L' -> newOccupiedTile = board.occupiedTiles[yPos][xPos - 1];
            case 'R' -> newOccupiedTile = board.occupiedTiles[yPos][xPos + xSize];
            default -> newOccupiedTile = false;
        }

        return newOccupiedTile;
    }


    private boolean checkBorderCollisions(char direction) {
        return switch (direction) {
            case 'U' -> !(yPos > 0);
            case 'D' -> !(yPos + ySize < 6);
            case 'L' -> !(xPos > 0);
            case 'R' -> !(xPos + xSize < 6);
            default -> true;
        };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addMainCar() {
        mainCar = true;
        this.setIcon(new ImageIcon("images/main_car.png"));
        revalidate();
    }

    private void addImage(int length, int direction) {
        if (length == 3) {
            if (direction == 0) {
                this.setIcon(new ImageIcon("images/purple_car_long_h.png"));
            } else
                this.setIcon(new ImageIcon("images/purple_car_long_v.png"));
        }
        if (length == 2) {
            if (direction == 0)
                this.setIcon(new ImageIcon("images/orange_car_short_h.png"));
            else
                this.setIcon(new ImageIcon("images/orange_car_short_v.png"));
        }
        revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
