package ch.bbbaden.io.game;

import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * author simon kappeler Created At: 03.05.2021
 */
public class EventHandlers {

    private static EventHandlers evt = new EventHandlers();

    public static EventHandlers getInstance() {
        return evt;
    }
    Stats stats = Stats.getInstance();

    public void minusToken() {
        int newTokens = stats.getUpgradeTokens();
        newTokens--;
        FXGL.inc("upgradeTokens", -1);
        stats.setUpgradeTokens(newTokens);
        stats.changeBackgroundColour();
    }
    EventHandler onUpgradeSpeed = new EventHandler() {
        @Override
        public void handle(Event t) {
            if (stats.getUpgradeTokens() > 0) {
                int newSpeed = stats.getSpeed();
                newSpeed++;
                stats.setSpeed(newSpeed);
                minusToken();
            }
        }
    };

    EventHandler onUpgradeHp = new EventHandler() {
        @Override
        public void handle(Event t) {
            if (stats.getUpgradeTokens() > 0) {
                FXGL.inc("hp", 5);
                minusToken();
            }
        }
    };
    EventHandler onUpgradeBulletSpeed = new EventHandler() {
        @Override
        public void handle(Event t) {
            if (stats.getUpgradeTokens() > 0) {
                int newSpeed = stats.getBulletSpeed();
                newSpeed += 50;
                stats.setBulletSpeed(newSpeed);
                minusToken();
            }
        }
    };
    EventHandler onUpgradeReload = new EventHandler() {
        @Override
        public void handle(Event t) {
            if (stats.getUpgradeTokens() > 0) {
                int newReload = stats.getReload();
                if (newReload - 1 > 0) {
                    newReload--;
                }
                stats.setReload(newReload);
                minusToken();
            }
        }
    };

    EventHandler onUpgradeRegen = new EventHandler() {
        @Override
        public void handle(Event t) {

        }
    };
    EventHandler onUpgradePlayer = new EventHandler() {
        @Override
        public void handle(Event t) {
            stats.incPlayerLvl(1);
            int x = (int) stats.getPlayer().getX();
            int y = (int) stats.getPlayer().getY();
            spawn("player", x, y);
            stats.getPlayer().removeFromWorld();
        }
    };

    public EventHandler getOnUpgradeSpeed() {
        return onUpgradeSpeed;
    }

    public EventHandler getOnUpgradeHp() {
        return onUpgradeHp;
    }

    public EventHandler getOnUpgradeBulletSpeed() {
        return onUpgradeBulletSpeed;
    }

    public EventHandler getOnUpgradeReload() {
        return onUpgradeReload;
    }

    public EventHandler getOnUpgradeRegen() {
        return onUpgradeRegen;
    }

    public EventHandler getOnUpgradePlayer() {
        return onUpgradePlayer;
    }

}
