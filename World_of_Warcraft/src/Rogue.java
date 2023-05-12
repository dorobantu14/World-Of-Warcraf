public class Rogue extends Character {

    public Rogue(Inventory potions, int currentLevel, int currentExperience) {
        characterName = "Rogue";
        this.currentLevel = currentLevel;
        this.potions = potions;
        earth = true;
        dexterity = 3 * currentLevel;
        charisma = 2 * currentLevel;
        strength = currentLevel;
        currentLife = 100;
        this.currentExperience = currentExperience;
        currentMana = 40;
        maxMana = 40;
        maxLife = 100;
    }

    //am folosit acelasi principiu de la Mage
    @Override
    void receiveDamage(int damage) {
        if ((charisma + strength) / 2 > damage) {
            currentLife = currentLife - (damage / 2);
        }
        else
            currentLife = currentLife - (damage - (charisma + strength) / 2);
    }

    @Override
    int getDamage() {
        return dexterity * 2 + strength + charisma;
    }
}
