import java.util.ArrayList;

public class Shop implements CellElement {
    ArrayList<Potion> shopPotionList = new ArrayList<>();

    public Shop(Potion potion1, Potion potion2) {
        shopPotionList.add(potion1);
        shopPotionList.add(potion2);
    }

    Potion getPotion(int index) {
        return shopPotionList.get(index);
    }

    Potion deletePotion(int index) {
        return shopPotionList.remove(index);
    }

    @Override
    public String toCharacter() {
        return "Shop";
    }
}
