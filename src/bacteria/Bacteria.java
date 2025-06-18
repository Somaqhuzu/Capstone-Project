package bacteria;

import java.util.concurrent.ThreadLocalRandom;

import bacteria.movement.MetropolisStep;
import energy.Energy;
import energy.OrganicMatter;

import environment.Surface;
import environment.SurfaceBlock;

public class Bacteria extends Thread{
    private  int[] position; //Accessed by Animating Thread, and Modified by itself
    private int id;
    private Energy nutritious = new OrganicMatter(50); // Removed or comment out since Energy is undefined
    //private Energy hamiltonian = new Hamiltonian

    private boolean isAttached = false;
    
    public Bacteria(int id,int x,int y){
        this.id = id;
        this.position = new int[]{x,y};
    }

    public Bacteria(int x, int y){
        this.position = new int[]{x, y};
        nutritious.addEnergy(100);
        
    }

    /*
     * @return returns vector position, in cartesian form
     */
    public int[] get2DPosition(){
        return position;
    }

    private void releaseEPS(){
        int[] epsNodePosition = new int[]{position[0] + (int)Math.signum(ThreadLocalRandom.current().nextInt(3)),position[1] + (int)Math.signum(ThreadLocalRandom.current().nextInt(3) - 1)};
        Surface.epsNodePositions().add(epsNodePosition);
    }
    public void run(){
        //Bacteria movement logic
        //This could be a random walk or some other behavior
        while(true){
            if(isAttached){
                try{
                    System.out.println("Bacteria " + id + " hirbenating");
                    releaseEPS();
                    wait(10000);} //Bacteria hibernate;
                catch(InterruptedException e){
                    notify();
                    nutritious.addEnergy(6);
                    System.out.println("Bacteria " + id + " wokeUp");
                }
            }
            try {
                Thread.sleep(600); // Simulate time passing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Update position logic here
            // For example, move randomly within bounds of the surface
            
            //Random Walks
            //Still to implement metropolisStep
            int[] nextPosition = new int[]{position[0] + (int)Math.signum(ThreadLocalRandom.current().nextInt(3) - 1),position[1] + (int)Math.signum(ThreadLocalRandom.current().nextInt(3) - 1)};
            SurfaceBlock nextSite = Surface.getSurface(nextPosition);
            try{
                if(nextSite.acquireBlock()){
                    if(MetropolisStep.metropolisTest(nextSite.getNutrient(), nextSite.getTemperature() - Surface.getSurface(position).getTemperature())){
                        Surface.getSurface(position).setOccupied(false); // Leave the previously occupied block
                        position[0] = nextPosition[0];
                        position[1] = nextPosition[1];

                        nutritious.addEnergy(nextSite.getNutrient());
                        nextSite.resetNutrients();
                    }
                    else nextSite.setOccupied(false); // If the move is not accepted, release the block
                    if(position[1] == 1) isAttached = true;
                    System.out.println("("+position[0] + "," + position[1] + ")");
                }
            }
            catch(NullPointerException e){}
        }
    
    }

    @Override
    public String toString(){
        return "Bacteria: " + id + '\n' + "Position(x,y): (" + position[0] + "," + position[1] + ")" + "\nEnergy: " + nutritious.getEnergy() + ""  ;
    }
}