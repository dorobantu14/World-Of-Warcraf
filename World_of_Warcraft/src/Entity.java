import java.awt.*;

public abstract class Entity {
    List abilityList;
    int currentMana;
    int maxMana;
    int currentLife;
    int maxLife;
    boolean fire;
    boolean ice;
    boolean earth;

    void regenerateLife(int life) {
        if (currentLife + life > maxLife) {
            currentLife = maxLife;
        } else {
            currentLife = currentLife + life;
        }
    }

    void regenerateMana(int mana) {
        if (currentMana + mana > maxLife) {
            currentMana = maxMana;
        } else {
            currentMana = currentMana + mana;
        }
    }

    void useAbility(boolean ability, Enemy enemy, Character chosenCharacter) {
        int fireCost = new Fire().manaCost;
        int fireDamage = new Fire().damage;
        int iceDamage = new Ice().damage;
        int iceCost = new Ice().manaCost;
        int earthCost = new Earth().manaCost;
        int earthDamage = new Earth().damage;

        //am ales ca daca abilitatea folosita este aceeasi cu cea a enemy-ului, enemy sa primeasca damage-ul abilitatii/2 iar
        //caracterul sa il primeasca impartit la 3
        // insa daca sunt diferite enemy primeste damageul intreg al abilitatii caracterului, iar caracterul primeste
        //damageul enemy-ului / 2
        if (ability == chosenCharacter.ice) {
            if (chosenCharacter.currentMana - fireCost >= 0) {
                if (enemy.ice) {
                    enemy.receiveDamage(iceDamage / 2);
                    chosenCharacter.receiveDamage(iceDamage / 3);
                } else if (enemy.fire) {
                    enemy.receiveDamage(iceDamage);
                    if (enemy.currentMana - fireCost >= 0) {
                        chosenCharacter.receiveDamage(fireDamage / 2);
                        enemy.currentMana = enemy.currentMana - fireCost;
                    }
                } else if (enemy.earth) {
                    enemy.receiveDamage(iceDamage);
                    if (enemy.currentMana - earthCost >= 0) {
                        chosenCharacter.receiveDamage(earthDamage / 2);
                        enemy.currentMana = enemy.currentMana - earthCost;
                    }
                }
                chosenCharacter.currentMana = chosenCharacter.currentMana - iceCost;
            }
        }

        if (ability == chosenCharacter.fire) {
            if (chosenCharacter.currentMana - fireCost >= 0) {
                if (enemy.fire) {
                    enemy.receiveDamage(fireDamage / 2);
                    chosenCharacter.receiveDamage(fireDamage / 3);
                } else if (enemy.ice) {
                    enemy.receiveDamage(fireDamage);
                    if (enemy.currentMana - fireCost >= 0) {
                        chosenCharacter.receiveDamage(iceDamage / 2);
                        enemy.currentMana = enemy.currentMana - iceCost;
                    }

                } else if (enemy.earth) {
                    enemy.receiveDamage(fireDamage);
                    if (enemy.currentMana - fireCost >= 0) {
                        chosenCharacter.receiveDamage(earthDamage / 2);
                        enemy.currentMana = enemy.currentMana - earthCost;
                    }
                }
                chosenCharacter.currentMana = chosenCharacter.currentMana - fireCost;
            }
        }

        if (ability == chosenCharacter.earth) {
            if (chosenCharacter.currentMana - earthCost >= 0) {
                if (enemy.earth) {
                    enemy.receiveDamage(earthDamage / 2);
                    chosenCharacter.receiveDamage(earthDamage / 3);
                } else if (enemy.ice) {
                    enemy.receiveDamage(earthDamage);
                    if (enemy.currentMana - iceCost >= 0) {
                        chosenCharacter.receiveDamage(iceDamage / 2);
                        enemy.currentMana = enemy.currentMana - iceCost;
                    }
                } else if (enemy.fire) {
                    enemy.receiveDamage(earthDamage);
                    if (enemy.currentMana - fireCost >= 0) {
                        chosenCharacter.receiveDamage(fireDamage);
                        enemy.currentMana = enemy.currentMana - fireCost;
                    }
                }
                chosenCharacter.currentMana = chosenCharacter.currentMana - earthCost;
            }
        }
    }

    abstract void receiveDamage(int damage);

    abstract int getDamage();

}
