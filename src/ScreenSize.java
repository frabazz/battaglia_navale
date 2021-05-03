import java.awt.*;

public class ScreenSize {
    Dimension screenSize;
    int width;
    int heigth;
    ScreenSize(){
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.width;
        heigth = screenSize.height;
    }

}
