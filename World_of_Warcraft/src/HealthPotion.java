public class HealthPotion implements Potion {
    int price;
    int valueOfRegeneration;
    int weightOfPotion;
    String potionName;

    //creez un contructor prin care atribui potiunii niste
    //valori fixe
    public HealthPotion() {
        potionName = "Health Potion";
        price = 25;
        valueOfRegeneration = 25;
        weightOfPotion = 1;
    }

    @Override
    public String getString() {
        return potionName;
    }

    @Override
    public void usePotion(Character player) {
       player.regenerateLife(valueOfRegeneration);
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
        return weightOfPotion;
    }
}
