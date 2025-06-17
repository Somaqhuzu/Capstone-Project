package bacteria;

import java.util.concurrent.ThreadLocalRandom;
//import energy.Energy;
//import energy.OrganicMatter;

public class Bacteria extends Thread{
    private int[] position;
    private int[] prevPosition;

    //private Energy nutritious = new OrganicMatter(); // Removed or comment out since Energy is undefined
    //private Energy hamiltonian = new Hamiltonian
    public Bacteria(int x, int y){
        this.position = new int[]{x, y};
        this.prevPosition = new int[]{0, 0};
        
    }
 
    public int getPosX(){
        return position[0];
    }

    public int getPosY(){
        return position[1];
    }

    public int getPrevPosX(){
        return prevPosition[0];
    }

    public int getPrevPosY(){
        return prevPosition[1];
    }   

    public void run(){
        //Bacteria movement logic
        //This could be a random walk or some other behavior
        while(true){
            try {
                Thread.sleep(1000); // Simulate time passing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Update position logic here
            // For example, move randomly within bounds of the surface
            prevPosition[0] = position[0];
            prevPosition[1] = position[1];
            //Random Walks
            //Still to implement metropolisStep
            position[0] += Math.signum(ThreadLocalRandom.current().nextInt(3) - 1); // Move left, right, or stay
            position[1] += Math.signum(ThreadLocalRandom.current().nextInt(3) - 1); // Move up, down, or stay
        }
    }
}