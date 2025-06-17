package energy;
//If quantitiy > 0, then the organic matter is nutritious, thus can be consumed by the bacteria
//else if quantity < 0, then it is an organic toxin
public class OrganicMatter implements Energy{
    double quantity;
    @Override
    public double getEnergy() {
       return quantity;
    }

    @Override
    public void addEnergy(double amount) {
        quantity += amount;
    }

}