import java.util.Vector;
/*
*
* La classe player si occupa di gestire la parte logica
* del giocatore e consta dei metodi della classe grid, che eredita.
*
*
*
* */
public class Player extends Grid{
    /*
    * 0 -> vuota
    * 1 -> barca
    * 2 -> affondata
    * 3 -> acqua
    * */
    Vector<int[]> randomCoordinates;
    private String username;
    private int[][] M;

    Player(String username, int width, int height, int[] boatsLength, boolean automaticSpawning){
        super(width, height);
        this.username = username;
        super.generateBoats(boatsLength);
        this.M = super.getMatrix();
        randomCoordinates = new Vector<int[]>();
    }

    Player(String username, int width, int height, int[] boatsLength){
        super(width, height, boatsLength);
        this.username = username;
        M = super.getMatrix();
    }

    public void sinkBoat(int x, int y){
        if(super.isBoat(x, y)){
            int[][] coordinates = super.getBoat(x, y).getCoordinates();
            for(int i = 0;i < coordinates.length;i++){
                M[coordinates[i][0]][coordinates[i][1]] = 2;
            }
        }
    }
    public int[][] getMatrix(){
        return M;
    }

    public void setWater(int x, int y){
        M[x][y] = 3;
    }

    public int getCell(int x, int y){
        return M[x][y];
    }

    public void printPlayerGrid(){
        for(int i = 0;i < M.length;i++){
            for(int j = 0;j < M[i].length;j++){
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void printEnemyGrid(){
        for(int i = 0;i < M.length;i++){
            for(int j = 0;j < M[i].length;j++){
                if(M[i][j] != 0 && M[i][j] != 1)System.out.print(M[i][j] + " ");
                else System.out.print("-1 ");
            }
            System.out.println();
        }
    }

    public boolean setBoat(int x, int y, int orientation, int length, int index){
        Boat b = new Boat(x, y, orientation, length);
        if(b.errorBoat(M)){
            return false;
        }
        else{
            super.addBoatIndex(b, index);
            M = super.getMatrix();
            return true;
        }
    }

    private boolean usedRandomCoordinates(int[] coordinates){
        for(int i = 0;i < randomCoordinates.size();i++){
            int[] usedCoordinates = randomCoordinates.get(i);
            if(coordinates[0] == usedCoordinates[0] && coordinates[1] == usedCoordinates[1])return true;
        }
        return false;
    }


    public int[] getRandomCoordinates(){
        int[] coordinates = {0, 0};
        int randomLaps = 0;
        do{
            coordinates = super.getRandomCoordinates();
            coordinates[0] = Math.abs(coordinates[0]);
            coordinates[1] = Math.abs(coordinates[1]);
            randomLaps++;
        }while(usedRandomCoordinates(coordinates) && randomLaps<100);
        if(randomLaps>90){
            int[][] matrix = super.getMatrix();
            for(int i = 0;i < matrix.length;i++){
                for(int j = 0;j < matrix.length;j++){
                    if(M[i][j] == 1){
                        coordinates[0] = i;
                        coordinates[1] = j;
                        break;
                    }
                }
            }
        }
        randomCoordinates.add(coordinates);
        return coordinates;

    }

    public boolean isBoat(int x, int y){
        System.out.println();
        super.printGrid();
        return super.isBoat(x, y);
    }

    public boolean hasLost(){
        for(int i = 0;i < M.length;i++){
            for(int j = 0;j < M[i].length;j++){
                if(M[i][j] == 1)return false;
            }
        }
        return true;
    }
}
