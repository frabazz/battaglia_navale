import java.awt.*;
import java.awt.event.*;

/*
 * +1 -> x+
 * +2 -> y+
 * -1 -> x-
 * -2 -> y-
 * */

/*

 * 0 -> vuota
 * 1 -> barca
 * 2 -> affondata
 * 3 -> acqua
 * -1 -> error
 * -2 -> red && nemico che sta selezionando
  */
public class GraphicGrid extends Canvas implements MouseListener, MouseMotionListener, MouseWheelListener {
    int[][] M;
    static int[][] originalM;
    int cellSize;
    int letters;

    public Boat[] getBoats() {
        return boats;
    }

    Boat[] boats;int boatIndex = 0;
    boolean isEnemy, mouseClicked, boatError, mouseEntered;
    private int mouseX, mouseY, orientation;
    boolean toRepaint;
    int refreshTime=20;
    Thread paintThread;

    GraphicGrid(int[][] M, int cellSize, int[] boatsLength){
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        this.M = M;
        this.boats = new Boat[boatsLength.length];
        originalM = this.M;
        this.cellSize = cellSize;
        letters = cellSize/2;
        isEnemy = false;
        initTimer();
    }

    GraphicGrid(int[][] M, int cellSize, boolean isEnemy){
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        this.M = M;
        originalM = this.M;
        this.cellSize = cellSize;
        this.isEnemy = isEnemy;
        letters = cellSize/2;
        initTimer();
    }


    public void initTimer(){
        paintThread = new Thread(new RepaintRunnable(this, refreshTime));
        paintThread.start();
    }


    public void removeListeners(){
        removeMouseListener(this);
        removeMouseMotionListener(this);
        removeMouseWheelListener(this);
    }

    public void delay(int millis){
        try{
            Thread.sleep(millis);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public void drawGrid(Graphics g){
        int width = (M.length+1)*cellSize;
        int height = (M[0].length+1)*cellSize;
        for(int i = 1;i <= M[0].length;i++){
            String s = "";
            s+=(char)(65+i-1);
            g.drawString(s, i*cellSize+ letters, cellSize- letters);
        }
        for(int i = 1;i <= M.length;i++){
            String s = String.valueOf(i);
            g.drawString(s,cellSize- letters, i*cellSize+ letters);
        }
        for(int i = 1;i <= M.length+1;i++){
            g.drawLine(cellSize, i*cellSize, width, i*cellSize);
        }
        for(int i = 1;i <= M[0].length+1;i++){
            g.drawLine(i*cellSize, cellSize, i*cellSize, height);
        }
    }

    public void paintCell(Graphics g, Color c, int x, int y){
        g.setColor(c);
        g.fillRect((x+1)*cellSize, (y+1)*cellSize, cellSize, cellSize);
    }

    public void paintCross(Graphics g, int x, int y){
        Graphics2D g2d = (Graphics2D)g;
        int width = 2;
        g2d.setStroke(new BasicStroke(width));
        g.setColor(Color.RED);
        g2d.drawLine((x+1)*cellSize, (y+1)*cellSize, (x+2)*cellSize, (y+2)*cellSize);
        g2d.drawLine((x+2)*cellSize, (y+1)*cellSize, (x+1)*cellSize, (y+2)*cellSize);
    }

    public void paintString(Graphics g, String s, int x,  int y){
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 19));
        g.drawString(s, ((x+1)*(cellSize)+cellSize/5), (((y+2)*cellSize))-cellSize/5);
    }


    public boolean selectBoat(Player p, int l) {
        mouseClicked = false;
        orientation = 1;
        boatError = false;
        int[][] changedCoordinates = new int[l][2];
        int[] CoordinatesValues = new int[l];
        while(!mouseClicked){
            int Ymatrix = (mouseX/cellSize)-1;
            int Xmatrix = (mouseY/cellSize)-1;
            Boat b = new Boat(Xmatrix, Ymatrix, orientation, l);
            int[][] Coordinates = b.getCoordinates();

            for(int i = 0;i < Coordinates.length;i++){
                if(Coordinates[i][0] != changedCoordinates[i][0] || Coordinates[i][1] != changedCoordinates[i][1])toRepaint=true;
            }
            for(int i = 0;i < changedCoordinates.length;i++){
                M[changedCoordinates[i][0]][changedCoordinates[i][1]] = CoordinatesValues[i];
            }
            if(b.errorBoat(M)){
                boatError = true;
                for(int i = 0;i < Coordinates.length;i++){
                    if(!(Coordinates[i][0] < 0 || Coordinates[i][0] > (M.length-1) || Coordinates[i][1] < 0 || Coordinates[i][1] > (M.length-1))){
                        changedCoordinates[i] = Coordinates[i];
                        CoordinatesValues[i] = M[Coordinates[i][0]][Coordinates[i][1]];
                        M[Coordinates[i][0]][Coordinates[i][1]] = -2;
                    }

                    else{
                        changedCoordinates[i][0] = 0;
                        changedCoordinates[i][1] = 0;
                        CoordinatesValues[i] = -1;
                    }
                }
            }
            else{
                boatError = false;
                for(int i = 0;i < Coordinates.length;i++){
                    changedCoordinates[i] = Coordinates[i];
                    CoordinatesValues[i] = M[Coordinates[i][0]][Coordinates[i][1]];
                    M[Coordinates[i][0]][Coordinates[i][1]] = 1;
                }
                boats[boatIndex] = b;
            }
            delay(refreshTime/2);
        }
        System.out.println(boatError);
        if(boatError){
            for(int i = 0;i < changedCoordinates.length;i++){
                M[changedCoordinates[i][0]][changedCoordinates[i][1]] = CoordinatesValues[i];
            }
        }
        if(!boatError)boatIndex++;
        return !boatError;
    }

    public int[] selectEnemyCell(){
        boolean outOfGrid = false;
        int ChangedX=0, ChangedY=0, ChangedValue=M[0][0];
        int Xmatrix=(mouseX/cellSize)-1, Ymatrix=(mouseY/cellSize)-1;
        int[] Coordinates = {Xmatrix, Ymatrix};
        mouseClicked = false;
        while(!mouseClicked) {
            if((Coordinates[0] != ChangedX || Coordinates[1] != ChangedY) && mouseEntered)toRepaint = true;
            Ymatrix = (mouseX / cellSize) - 1;
            Xmatrix = (mouseY / cellSize) - 1;
            Coordinates[0] = Xmatrix;
            Coordinates[1] = Ymatrix;
            if((Coordinates[0])<0 || (Coordinates[1])<0 || (Coordinates[0])>(M.length-1) || (Coordinates[1])>(M.length-1))outOfGrid = true;
            else{
                M[ChangedX][ChangedY] = ChangedValue;
                ChangedValue = M[Coordinates[0]][Coordinates[1]];
                ChangedX = Coordinates[0];
                ChangedY = Coordinates[1];
                M[Coordinates[0]][Coordinates[1]] = -2;
                outOfGrid = false;
            }
            delay(refreshTime/2);
        }
        if(!(ChangedX<0 || ChangedY<0 || ChangedX>M.length-1 || ChangedY>M.length-1))M[ChangedX][ChangedY] = ChangedValue;
        if(!outOfGrid)M[Coordinates[0]][Coordinates[1]] = ChangedValue;
        return outOfGrid ? new int[]{-1, -1} : Coordinates;
    }

    public void setWater(int x, int y){
        M[x][y] = 3;
    }

    public void sinkBoat(Boat b){
        int[][] coordinates = b.getCoordinates();
        for(int i = 0;i < coordinates.length;i++){
            M[coordinates[i][0]][coordinates[i][1]] = 2;
        }
    }


    public void paint(Graphics g) {
        drawGrid(g);
        //Player grid paint
        if(!isEnemy) {
            for (int i = 0; i < M.length; i++) {
                for (int j = 0; j < M[0].length; j++) {
                    if(M[i][j] == 1) this.paintCell(g, Color.BLUE, j, i);
                    if(M[i][j] == -2) this.paintCell(g, Color.RED, j, i);
                    if(M[i][j] == 2)this.paintCross(g, j, i);
                    if (M[i][j] == 3)this.paintCell(g, Color.CYAN, j, i);
                }
            }
        }
        //Bot grid paint
        else{ ;
            for (int i = 0; i < M.length; i++) {
                for (int j = 0; j < M[0].length; j++) {
                    if (M[i][j] == 0 || M[i][j] == 1) this.paintString(g, "?", j, i);
                    if (M[i][j] == -2) this.paintCell(g, Color.BLUE, j, i);
                    if (M[i][j] == 2)this.paintCross(g, j, i);
                    if (M[i][j] == 3)this.paintCell(g, Color.CYAN, j, i);
                }
            }
        }
    }
    public void mouseClicked(MouseEvent e) {
        mouseClicked = true;
    }
    public void mouseEntered(MouseEvent e) {
        mouseEntered = true;
    }
    public void mouseExited(MouseEvent e) {
        mouseEntered = false;
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        switch(orientation){
            case 1:
                orientation++;
                break;
            case 2:
                orientation=-2;
                break;
            case -2:
                orientation++;
                break;
            case -1:
                orientation = 1;
                break;
            default:
                orientation = 1;
                break;
        }
    }


}
