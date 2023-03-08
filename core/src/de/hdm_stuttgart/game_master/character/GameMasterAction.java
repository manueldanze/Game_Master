package de.hdm_stuttgart.game_master.character;

public enum GameMasterAction {

    TACK_SHOOTER(TackShooter.class, 50), PISTOL_TROOPER(PistolTrooper.class, 25), ARCHER(Archer.class, 60), SUPER_ARCHER(SuperArcher.class, 150);

    public final Class<? extends Enemy> enemyType;
    private int cost;

    GameMasterAction(Class<? extends Enemy> type, int cost) {
        enemyType = type;
        this.cost = cost;
    }

    public int getCost() { return cost; }
}
