package environment;
//Building Block of a wall surface
//Element of a vector

import java.util.concurrent.atomic.AtomicBoolean;

import energy.Energy;
import energy.OrganicMatter;

//vector description
public class SurfaceBlock{
private int temperature; //in kelvin
private Energy nutrients; //Nutrients
private float epsDensity = 0.0f;


private int[] position; //Position in the vector(2D) for now
private AtomicBoolean occupancy = new AtomicBoolean(false); //To check if the block is occupied by a bacteria


public SurfaceBlock(int x, int y,int nutrient){
    this.position = new int[]{x, y};
    this.temperature = (int) Math.random() * 100; 
    this.nutrients = new OrganicMatter(nutrient); 
    }

    public SurfaceBlock(int x, int y){
    this.position = new int[]{x, y};
    this.temperature = (int) Math.random(); //Default value in Kelvin
    this.nutrients = new OrganicMatter(0);
    }

    
    public int[] getPosition(){
        return position;
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

    public double getNutrient(){
        return nutrients.getEnergy();
    }

    public void resetNutrients(){
        nutrients.addEnergy(-nutrients.getEnergy());
    }

    public void addEPSDensity(float density){
        if(epsDensity < 1.0f)
            this.epsDensity += density;
    }

    public float getEPSDensity(){
        return epsDensity;
    }
    

}