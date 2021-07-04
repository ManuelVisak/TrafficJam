import javax.swing.*;

public class Main extends JFrame {

    public Main(){
        Board b = new Board();
        this.add(b);
        this.setTitle("Traffic Jam");
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    public static void main(String[] sdfs) {
        new Main();
    }

    //die Änderung
}