public abstract class Character extends Entity {
    String characterName;
    int coordonateX;
    int coordonateY;
    Inventory potions;
    int currentExperience;
    int currentLevel;
    int strength;
    int charisma;
    int dexterity;

    void buyPotion(Potion potion, Inventory inventory) {
        if (potion.getPrice() <= inventory.numberOfCoins) {
            potions.addPotion(potion);
            potions.numberOfCoins = potions.numberOfCoins - potion.getPrice();
        }
        else
            System.out.println("You don't have money :(");
    }
}
