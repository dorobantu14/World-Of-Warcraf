import java.util.ArrayList;
import java.util.Random;

public class Grid extends ArrayList {
    int latime;
    int lungime;
    Cell currentCell;
    private static Grid grid;

    private Grid(int latime, int lungime, Cell currentCell) {
        this.latime = latime;
        this.lungime = lungime;
        this.currentCell = currentCell;
    }

    public static Grid getGrid() {
        grid = new Grid(5, 5, new Cell(0, 0, Cell.CellType.EMPTY));
        return grid;
    }

    ArrayList<Cell>[] map = new ArrayList[60];

    //metoda pentru obtinerea mapei cu numar random de shop-uri si inamici
    void getMap(int lungime, int latime, boolean bool) {
        int numberOfShops = new Random().nextInt(4, 6);
        int numberOfEnemies = new Random().nextInt(6, 8);
        ArrayList<Cell> shops = new ArrayList<>();
        ArrayList<Cell> enemies = new ArrayList<>();

        for (int i = 0; i < lungime; i++) {
            map[i] = new ArrayList<>();
            for (int j = 0; j < latime; j++) {
                map[i].add(j, new Cell(i, j, Cell.CellType.EMPTY));
                map[i].get(j).isChecked = false;
            }
        }

        if (bool) {
            while (numberOfEnemies != 0) {
                enemies.add(new Cell(new Random().nextInt(latime), new Random().nextInt(latime), Cell.CellType.ENEMY));
                numberOfEnemies -= 1;
            }

            while (numberOfShops != 0) {
                shops.add(new Cell(new Random().nextInt(latime), new Random().nextInt(latime), Cell.CellType.SHOP));
                numberOfShops -= 1;
            }

            for (Cell shop : shops) {
                if (map[shop.coordonateOx].get(shop.coordonateOy).type == Cell.CellType.EMPTY) {
                    map[shop.coordonateOx].add(shop.coordonateOy, shop);
                    map[shop.coordonateOx].get(shop.coordonateOy).enemyOrShop = new Shop(new ManaPotion(), new HealthPotion());
                }
            }
            for (Cell enemy : enemies) {
                if (map[enemy.coordonateOx].get(enemy.coordonateOy).type == Cell.CellType.EMPTY) {
                    map[enemy.coordonateOx].add(enemy.coordonateOy, enemy);
                    map[enemy.coordonateOx].get(enemy.coordonateOy).enemyOrShop = new Enemy(2, 20);
                }
            }

            map[latime - 1].add(lungime - 1, new Cell(latime - 1, lungime - 1, Cell.CellType.FINISH));

        } else {
            map[0].add(3, new Cell(0, 3, Cell.CellType.SHOP));
            map[1].add(3, new Cell(1, 3, Cell.CellType.SHOP));
            map[2].add(0, new Cell(2, 0, Cell.CellType.SHOP));
            map[3].add(4, new Cell(3, 4, Cell.CellType.ENEMY));
            map[4].add(4, new Cell(4, 4, Cell.CellType.FINISH));
        }
    }

    //am creeat o metoda de printare a mapei bazata de ce celule au fost vizate
    void printMap(Character character) {
        for (int j = 0; j < lungime; j++) {
            for (int k = 0; k < lungime; k++) {
                System.out.print(j == currentCell.coordonateOx && k == currentCell.coordonateOy ? character.characterName +
                        "    " : (!map[j].get(k).isChecked ? "?     " : (map[j].get(k).enemyOrShop != null ?
                        map[j].get(k).enemyOrShop.toCharacter() + "   " : map[j].get(k).type + "   ")));
            }
            System.out.println("\n");
        }
    }

    //implementez metodele pentru miscari
    void goNorth() {
        if (currentCell.coordonateOx > 0) {
            currentCell.coordonateOx -= 1;
        }
    }

    void goSouth() {
        if (currentCell.coordonateOx < lungime) {
            currentCell.coordonateOx += 1;
        }
    }

    void goEast() {
        if (currentCell.coordonateOy < latime) {
            currentCell.coordonateOy += 1;
        }
    }

    void goWest() {
        if (currentCell.coordonateOy > 0) {
            currentCell.coordonateOy -= 1;
        }
    }
}
