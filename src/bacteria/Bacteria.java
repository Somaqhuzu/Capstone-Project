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
    private Energy nutritious = new OrganicMatter(5); // Removed or comment out since Energy is undefined
    //private Energy hamiltonian = new Hamiltonian

    private boolean isAttached = false;
    
    public Bacteria(int id,int x,int y){
        this.id = id;
        this.position = new int[]{x,y};
    }

    /*
     * @return returns vector position, in cartesian form
     */
    public int[] get2DPosition(){
        return position;
    }

    public void reproduce(){
        //Reproduction logic here
        //For example, create a new Bacteria at the current position with some energy
        Bacteria offspring = new Bacteria(id + 1, position[0] + ThreadLocalRandom.current().nextInt(3) - 1, position[1] + ThreadLocalRandom.current().nextInt(2));
        //offspring.nutritious.addEnergy(nutritious.getEnergy() / 2); // Share energy with offspring
        nutritious.addEnergy(-nutritious.getEnergy() / 2); // Reduce energy of parent
        // Add the offspring to the surface or a list of bacteria
        Surface.addBacteria(offspring); // Assuming there's a method to add bacteria to the population
        offspring.start(); // Start the offspring thread
        System.out.println("Bacteria " + id + " reproduced: " + offspring);
        Surface.getSurface(position).setOccupied(false); // Release the block after reproduction
    }

    private void releaseEPS(){
        //int[] epsNodePosition = new int[]{position[0] + (int)Math.signum(ThreadLocalRandom.current().nextInt(3)),position[1] + (int)Math.signum(ThreadLocalRandom.current().nextInt(3) - 1)};
        int[] affectedPositionX = new int[]{-3,-2,-1,1,2,3};
        int[] affectedPositionY = new int[]{0,1,2};

        for(int i = 0;i<affectedPositionX.length;i++){
            for(int j = 0;j<affectedPositionY.length;j++){
                int[] affectedPosition = new int[]{position[0] + affectedPositionX[i],position[1] + affectedPositionY[j]};
                SurfaceBlock affectedBlock = Surface.getSurface(affectedPosition);
                if(affectedBlock != null){
                    affectedBlock.addEPSDensity(0.1f);
                }
            }
        }
        Surface.epsNodePositions().add(position); // Add the current position to the EPS nodes
    }
    public void run(){
        //Bacteria movement logic
        //This could be a random walk or some other behavior
        while(true){
            while(Surface.started){
                try {
                    Thread.sleep(100); // Wait until the simulation starts
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            
            if(isAttached){
                try{
                    System.out.println("Bacteria " + id + " hirbenating");
                    releaseEPS();
                    wait(1000);} //Bacteria hibernate;
                catch(InterruptedException e){
                    notify();
                    System.out.println("Bacteria " + id + " wokeUp");
                }
            }

            if(nutritious.getEnergy() >= 10){
                reproduce(); // Reproduce if energy is high

            }

            try {
                System.out.println(this);
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
                if(nextSite!=null && nextSite.acquireBlock()){
                    if(MetropolisStep.metropolisTest(nextSite.getNutrient(), nextSite.getTemperature() - Surface.getSurface(position).getTemperature())){
                        Surface.getSurface(position).setOccupied(false); // Leave the previously occupied block
                        position[0] = nextPosition[0];
                        position[1] = nextPosition[1];

                        nutritious.addEnergy(nextSite.getNutrient());
                        nextSite.resetNutrients();
                    }
                    else nextSite.setOccupied(false); // If the move is not accepted, release the block
                    if(position[1] == 1) isAttached = true;
                    else if(nextSite.getEPSDensity() > ThreadLocalRandom.current().nextFloat() && !isAttached){
                        isAttached = true;
                        //nutritious.addEnergy(10);
                    }
                    System.out.println("("+position[0] + "," + position[1] + ")");
                }
            }
            catch(NullPointerException e){}
        }
    }
    }

    @Override
    public String toString(){
        return "Bacteria: " + id + '\n' + "Position(x,y): (" + position[0] + "," + position[1] + ")" + "\nEnergy: " + nutritious.getEnergy() + ""  ;
    }
}