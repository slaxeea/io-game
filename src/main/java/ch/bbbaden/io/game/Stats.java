package ch.bbbaden.io.game;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * author simon kappeler Created At: 03.05.2021
 */
public class Stats {

    private static Stats stats = new Stats();

    private int bulletSpeed = 200;
    private int height;
    private int width;
    private int speed = 1;

    public Stats() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        height = (int) (screen.getHeight() - ((screen.getHeight() / 100) * 20));
        width = (int) (screen.getWidth() - ((screen.getWidth() / 100) * 20));
    }

    public static Stats getInstance() {
        return stats;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
