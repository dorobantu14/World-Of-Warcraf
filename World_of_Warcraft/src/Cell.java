public class Cell {
    int coordonateOx;
    int coordonateOy;
    CellElement enemyOrShop;
    CellType type;
    boolean isChecked;

    public Cell(int x, int y, CellType cellType) {
        coordonateOx = x;
        coordonateOy = y;
        type = cellType;
    }


    enum CellType {EMPTY, ENEMY, SHOP, FINISH}
}
