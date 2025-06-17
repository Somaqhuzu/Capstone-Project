package environment;
//Building Block of a wall surface
//Element of a vector

import java.util.concurrent.atomic.AtomicBoolean;

//vector description
public class SurfaceBlock{
//private int humid;//0 - 100 %
private int temperature; //in kelvin
private int organicNutrient; //Food
//private int organicToxins; //Excreation etc et

private int[] position; //Position in the vector(2D) for now
private AtomicBoolean occupancy = new AtomicBoolean(false); //To check if the block is occupied by a bacteria

public SurfaceBlock(int x, int y,int nutrient){
    this.position = new int[]{x, y};
    //this.humid = 0; //Default value
    this.temperature = (int) Math.random(); //Default value in Kelvin
    this.organicNutrient = nutrient; //Default value
    //this.organicToxins = 0; //Default value
    }

    public SurfaceBlock(int x, int y){
    this.position = new int[]{x, y};
    //this.humid = 0; //Default value
    this.temperature = (int) Math.random(); //Default value in Kelvin
    this.organicNutrient = 0; //Default value
    //this.organicToxins = 0; //Default value
    }

    
    public int[] getPosition(){
        return position;
    }

    public boolean isOccupied(){
        return occupancy.get();
    }

    public void setOccupied(boolean occupied){
        occupancy.set(occupied);
    }
    public boolean acquireBlock(){
        return occupancy.compareAndSet(false, true);
    }

    public int getTemperature(){
        return temperature;
    }

    public int getNutrient(){
        return organicNutrient;
    }

    public void setNutrient(int i){
        organicNutrient = i;
    }
}