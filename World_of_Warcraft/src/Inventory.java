import java.awt.*;
import java.util.ArrayList;

public class Inventory {
    ArrayList<Potion> potionList;
    int maxWeight;
    int numberOfCoins;

    public Inventory(int maxWeight, int numberOfCoins, ArrayList<Potion> potionList) {
        this.maxWeight = maxWeight;
        this.numberOfCoins = numberOfCoins;
        this.potionList = potionList;
    }

    void addPotion(Potion potion) {
        potionList.add(potion);
    }

    void deletePotion(Potion potion) {
        potionList.remove(potion);
    }

    int getCurrentWeight() {
        return 1;
    }

}
