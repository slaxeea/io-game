package ch.bbbaden.io.game;

import com.almasb.fxgl.dsl.FXGL;
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
        stats.setUpgradeTokens(newTokens);
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
}
