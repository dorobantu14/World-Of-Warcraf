public class ManaPotion implements Potion {
    int price;
    int valueOfRegeneration;
    int weightOfPotion;
    String potionName;

    //creez un contructor prin care atribui potiunii niste
    //valori fixe
    public ManaPotion() {
        potionName = "Mana Potion";
        price = 15;
        valueOfRegeneration = 20;
        weightOfPotion = 1;
    }

    @Override
    public String getString() {
        return potionName;
    }

    @Override
    public void usePotion(Character player) {
        player.regenerateMana(valueOfRegeneration);
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getValueOfRegeneration() {
        return valueOfRegeneration;
    }

    @Override
    public int getWeightOfPotion() {
        return valueOfRegeneration;
    }
}
