import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepaintRunnable implements Runnable, ActionListener {
    GraphicGrid grid;
    Timer timer;
    int refreshTime;
    boolean toRepaint;

    public void setToRepaint(boolean toRepaint){
        this.toRepaint = toRepaint;
    }

    RepaintRunnable(GraphicGrid grid, int refreshTime){
        this.grid = grid;
        this.refreshTime = refreshTime;
    }

    public void run(){
       timer = new Timer(refreshTime, this);
       timer.start();
    }

    public void actionPerformed(ActionEvent e) {
      grid.repaint();
    }
}
