package bacteria.movement;

import java.util.concurrent.ThreadLocalRandom;

public class MetropolisStep {
    //Responsible for movement of bacteria within the surface environment
    /*
     * Before a Bacteria(monomer) moves, it must check the following:
     * 1. If the position is occupied by another Bacteria(Monomer)
     * 2. If the movement is within the bounds
     * 3. Metropolis Test:
     */

    public static boolean metropolisTest(double dEnergy,double dTemperature){
        if(dEnergy>=0)return true;
        if(dTemperature ==0) dTemperature = 1;
        double probability = Math.pow(Math.E,-dEnergy/dTemperature);
        return probability > ThreadLocalRandom.current().nextDouble(0,1);
    }
     
}
