import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("sun.java2d.opengl", "true");//cosa fa questa riga? buh , va più veloce
        FileReader reader = new FileReader("src/config.text");
        int[] l = reader.fileError ? new int[]{2, 3, 5} : reader.getBoatsLength();
        Player bot = new Player("Enzo", 10, 10, l, true);
        JFrame frame = new JFrame("Battaglia Navale");
        frame.setSize(new Dimension(1200, 650));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container c = frame.getContentPane();
        ((JPanel)c).setDoubleBuffered(true);//cosa fa questa riga? buh , va più veloce 2.0
        frame.setVisible(true);
        int decision = JOptionPane.YES_OPTION;
        do{
            GameScreen gamescreen = new GameScreen(frame, "Turi", "enzo", l, 10, 10, 27);
            gamescreen.setupComponents();
            decision = gamescreen.gameLoop();
        }
        while(decision == JOptionPane.YES_OPTION);
        System.exit(2);

        System.out.println();

}
}
