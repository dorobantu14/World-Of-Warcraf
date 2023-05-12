public class Warrior extends Character {

    public Warrior(Inventory potions, int currentLevel, int currentExperience) {
        characterName = "Warrior";
        this.currentLevel = currentLevel;
        this.potions = potions;
        fire = true;
        strength = currentLevel * 3;
        charisma = currentLevel;
        dexterity = currentLevel * 2;
        currentLife = 100;
        this.currentExperience = currentExperience;
        currentMana = 40;
        maxMana = 40;
        maxLife = 100;
    }

    //am folosit acelasi principiu de la Mage
    @Override
    void receiveDamage(int damage) {
        if ((dexterity + charisma) / 2 > damage) {
            currentLife = currentLife - (damage / 2);
        }
        else
            currentLife = currentLife - (damage - (dexterity + charisma) / 2);
    }

    @Override
    int getDamage() {
        return strength * 2 + dexterity + charisma;
    }
}
