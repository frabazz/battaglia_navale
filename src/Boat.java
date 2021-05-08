import java.util.Random;

public class Boat {
    private int x0;
    private int y0;
    private int orientation;
    private int length;
    /*
    * +1 -> x+
    * +2 -> y+
    * -1 -> x-
    * -2 -> y-
    * */


    Boat(int x0, int y0, int orientation, int length){
        this.x0 = x0;
        this.y0 = y0;
        this.orientation = orientation;
        this.length = length;
    }

    Boat(int width, int height, int length, int[][] grid){
        this.length = length;
        do{
            Random rand = new Random(System.currentTimeMillis()%520402);
            x0 = (rand.nextInt()*99)%width;
            y0 = (rand.nextInt()*99)%height;
            orientation = (((rand.nextInt()*99))%2)+1;
            orientation = (((rand.nextInt()*99)%2))==0?orientation : orientation*-1;
        }
        while(thereIsBoat(grid));
    }

    public int[][] getCoordinates(){
        int xPlus = Math.abs(orientation)==1?0 : 1;
        int yPlus = Math.abs(orientation)==1?1 : 0;
        int sign = orientation>0?1 : -1;
        int x = x0, y = y0;
        int[][] coordinates = new int[length][2];
        for(int i = 0;i < length;i++){
            coordinates[i][0] = x;
            coordinates[i][1] = y;
            x+=xPlus*sign;
            y+=yPlus*sign;
        }
        return coordinates;
    }

    public boolean thereIsBoat(int grid[][]){
        int xPlus = Math.abs(orientation)==1?0 : 1;
        int yPlus = Math.abs(orientation)==1?1 : 0;
        int sign = orientation>0?1 : -1;
        int x = x0, y = y0;
        for(int i = 0;i < length;i++){
            if(x>(grid.length-1) || (y>grid[0].length-1) || x<0 || y<0)return true;
            if(grid[x][y] == 1)return true;
            x+=xPlus*sign;
            y+=yPlus*sign;
        }
        return false;
    }

    public boolean errorBoat(int grid[][]){
        int[][] coordinates = this.getCoordinates();
        for(int i = 0; i < coordinates.length;i++){
            int x = coordinates[i][0];
            int y = coordinates[i][1];
            if(x<0 || y<0 || x>(grid.length-1) || y>(grid.length-1) || grid[x][y] == 1)return true;
        }
        return false;
    }
}
