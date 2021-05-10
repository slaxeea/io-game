package ch.bbbaden.io.game;

import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import com.almasb.fxgl.entity.Entity;

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
    private int reload = 25;
    private int upgradeTokens = 0;
    private int upgradeScore = 0;

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

    public int getReload() {
        return reload;
    }

    public void setReload(int reload) {
        this.reload = reload;
    }

    public int getUpgradeTokens() {
        return upgradeTokens;
    }

    public void setUpgradeTokens(int upgradeTokens) {
        this.upgradeTokens = upgradeTokens;
    }

    public int getUpgradeScore() {
        return upgradeScore;
    }

    public void incUpgradeToken(int amount) {
        upgradeTokens += amount;
    }

    public void setUpgradeScore(int upgradeScore) {
        this.upgradeScore = upgradeScore;
    }

    public void incUpgradeScore(int amount) {
        upgradeScore += amount;
    }

    public void checkIfDead() {
        int hp = FXGL.geti("hp");
        if (hp < 1) {
            youDead();
        }
    }

    public void youDead() {
        System.exit(0);
    }

    public void spawnFood(String type) {
        Viewport viewport = getGameScene().getViewport();
        int x = Math.abs((int) stats.getPlayer().getX());
        int y = Math.abs((int) stats.getPlayer().getY());
        int maxX = x + getWidth();
        int maxY = y + getHeight();
        int randX = FXGL.random(x, maxX) - maxX;
        int randY = FXGL.random(y, maxY) - maxY;
        spawn(type, -randX, -randY);
    }

    public Entity getPlayer() {
        return getGameWorld().getSingleton(Entities.PLAYER);
    }
}
