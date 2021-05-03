package ch.bbbaden.io.game;

/**
 * author simon kappeler Created At: 03.05.2021
 */
public class Stats {

    private static Stats stats = new Stats();

    private int bulletSpeed = 200;

    public static Stats getInstance() {
        return stats;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

}
