import javax.swing.*;
import java.awt.*;

public class Drawing  extends Canvas {
    private JFrame frame;
    private int x, y;
    Drawing(JFrame frame, int x, int y){
        this.frame = frame;
        this.x = x;
        this.y = y;
    }
    public void drawCar(Graphics g){
        g.setColor(Color.BLACK);
        g.fillOval(10, 300, 100, 100);
        g.fillOval(300, 300, 100, 100);
        g.drawRect(10, 250, 400, 50);
        g.drawLine(10, 250, 100, 150);
        g.drawLine(100, 150, 310, 150);
        g.drawLine(310, 150, 410, 250);
    }
    public void updateGraphics(int x, int y){
        this.x = x;
        this.y = y;
        this.repaint();
    }
    public void paint(Graphics g){
        g.setColor(Color.RED);
        g.fillOval(x, y, 100, 100);
        System.out.printf("x : %d, y: %d\n", x, y);
    }
}
