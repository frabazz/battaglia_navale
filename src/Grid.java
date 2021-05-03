import java.awt.*;
import java.util.Random;

public class Grid {
    /*
    * 0 -> no nave
    * 1 -> nave
    * */
    private int width;
    private int height;
    private int M[][];
    private Boat[] boats;

    Grid(int width, int height){
        this.width = width;
        this.height = height;
        M = new int[width][height];
        for(int i = 0;i < width;i++){
            for(int j = 0;j < height;j++){
                M[i][j] = 0;
            }
        }
    }

    Grid(int width, int height, int[] boatsLength){
        this.width = width;
        this.height = height;
        M = new int[width][height];
        for(int i = 0;i < width;i++){
            for(int j = 0;j < height;j++){
                M[i][j] = 0;
            }
        }
        boats = new Boat[boatsLength.length];
    }
    public void generateBoats(int[] boatsLength){
        System.out.println("hai cagato?");
        boats = new Boat[boatsLength.length];
        for(int i = 0;i < boatsLength.length;i++){
            boats[i] = new Boat(width, height, boatsLength[i], M);
            int[][] coordinates = boats[i].getCoordinates();
            for(int j = 0;j < coordinates.length;j++){
                M[coordinates[j][0]][coordinates[j][1]] = 1;
            }
        }
    }

    public void printGrid(){
        for(int i = 0;i < width;i++){
            for(int j = 0;j < height;j++){
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }
    }
    public Boat getBoat(int x, int y){
        for(int i = 0;i < boats.length;i++){
            int[][] coordinates = boats[i].getCoordinates();
            for(int j = 0;j < coordinates.length;j++){
                if(coordinates[j][0] == x && coordinates[j][1] == y){
                    return boats[i];
                }
            }
        }
        return new Boat(-1, -1, -1, -1);//throw error
    }
    public boolean isBoat(int x, int y){
        if(M[x][y] == 1)return true;
        else{System.out.println(M[x][y]);}
        return false;
    }

    public void addBoatIndex(Boat b, int index){
        System.out.println("trying with index = " + index);
        boats[index] = b;
        int coordinates[][] = b.getCoordinates();
        for(int i = 0;i < coordinates.length;i++){
            M[coordinates[i][0]][coordinates[i][1]] = 1;
        }
    }

    public int[] getRandomCoordinates(){
        int coordinates[] = new int[2];
        Random rand = new Random(System.currentTimeMillis());
        coordinates[0] = (((int)(rand.nextInt()*1000))%width);
        coordinates[1] = (((int)(rand.nextInt()*1000))%height);
        return coordinates;
    }

    public int[][] getMatrix(){
        return M;
    }
}
