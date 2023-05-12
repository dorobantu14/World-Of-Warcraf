public class Mage extends Character {

    public Mage(Inventory potions, int currentLevel, int currentExperience) {
        characterName = "Mage";
        this.currentLevel = currentLevel;
        this.potions = potions;
        ice = true;
        charisma = currentLevel * 3;
        dexterity = currentLevel;
        strength = currentLevel * 2;
        maxLife = 100;
        maxMana = 40;
        currentLife = 100;
        this.currentExperience = currentExperience;
        currentMana = 40;
    }

    @Override
    void receiveDamage(int damage) {
        if ((dexterity + strength) / 2 > damage) {          //am decis ca daca media aritmetica a caracteristicilor secundare
            currentLife = currentLife - (damage / 2);       //este mai mare decat damage-ul primit, sa primeasca damage-ul injumatatit
        }
        else
            currentLife = currentLife - (damage - (dexterity + strength) / 2);  //altfel primeste damage-ul - media lor aritmetica
    }

    @Override
    int getDamage() {
        return charisma * 2 + dexterity + strength;
    }
}
