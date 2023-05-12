import java.util.Random;

public class Enemy extends Entity implements CellElement {
    public Enemy(int life, int mana) {
        currentLife = life;
        currentMana = mana;
        fire = new Random().nextBoolean();
        ice = new Random().nextBoolean();
        earth = new Random().nextBoolean();
    }

    @Override
    public String toCharacter() {
        return "Enemy";
    }

    @Override
    void receiveDamage(int damage) {
        currentLife = currentLife - damage;
    }

    @Override
    int getDamage() {
        return 20;                  //damage initial
    }
}
