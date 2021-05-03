package ch.bbbaden.io.game;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.input.Input;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.util.Map;
import java.util.ResourceBundle.Control;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * author simon kappeler Created At: 26.04.2021
 */
public class Starter extends GameApplication {

    private boolean autofire = false;
    private int autofireCount = 0;
    private int upgradeTokens = 0;
    private int upgradeScore = 0;
    private Stats stats = Stats.getInstance();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setIntroEnabled(false);
        settings.setTitle("Diep.io 2.0");
        settings.setFullScreenAllowed(true);
        settings.setHeight(stats.getHeight());
        settings.setWidth(stats.getWidth());
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.WHITE);
        getGameWorld().addEntityFactory(new Factory());

        spawn("player", getAppWidth() / 2, getAppHeight() / 2 - 30);

        for (int i = 0; i < FXGLMath.random(1, 3); i++) {
            spawn("food_triangle", FXGLMath.random(20, getAppWidth() - 20), FXGLMath.random(20, getAppHeight() - 20));
        }
        for (int i = 0; i < FXGLMath.random(0, 3); i++) {
            spawn("food_rectangle", FXGLMath.random(20, getAppWidth() - 20), FXGLMath.random(20, getAppHeight() - 20));
        }
        for (int i = 0; i < FXGLMath.random(0, 2); i++) {
            spawn("food_octagon", FXGLMath.random(20, getAppWidth() - 20), FXGLMath.random(20, getAppHeight() - 20));
        }
        run(() -> {
            double x = getAppWidth();
            double y = FXGLMath.random(0, getAppHeight() - 20);

        }, Duration.seconds(0.15));

    }

    @Override
    protected void initInput() {
        int speed = stats.getSpeed();
        onKey(KeyCode.W, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(-speed));
        onKey(KeyCode.S, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(speed));

        onKey(KeyCode.A, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(-speed));
        onKey(KeyCode.D, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(speed));

        onKey(KeyCode.O, () -> this.autofire = this.autofire ? false : true);

        onBtnDown(MouseButton.PRIMARY, () -> {
            double y = getGameWorld().getSingleton(Entities.PLAYER).getY();
            double x = getGameWorld().getSingleton(Entities.PLAYER).getX();
            spawn("projectile", x - 10, y);
            Input input = getInput();
        });
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Entities.PROJECTILE, Entities.FOOD_RECTANGLE, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            spawn("food_rectangle", FXGLMath.random(20, getAppWidth() - 20), FXGLMath.random(20, getAppHeight() - 20));
            FXGL.inc("score", +1);
            upgradeScore += 1;
        });
        onCollisionBegin(Entities.PROJECTILE, Entities.FOOD_TRIANGLE, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            spawn("food_triangle", FXGLMath.random(20, getAppWidth() - 20), FXGLMath.random(20, getAppHeight() - 20));
            FXGL.inc("score", +2);
            upgradeScore += 2;
        });
        onCollisionBegin(Entities.PROJECTILE, Entities.FOOD_OCTAGON, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            spawn("food_octagon", FXGLMath.random(20, getAppWidth() - 20), FXGLMath.random(20, getAppHeight() - 20));
            FXGL.inc("score", +5);
            upgradeScore += 5;
        });

        onCollisionBegin(Entities.PLAYER, Entities.FOOD_RECTANGLE, (player, rect) -> {
            FXGL.inc("hp", -1);
            checkIfDead();
        });
        onCollisionBegin(Entities.PLAYER, Entities.FOOD_TRIANGLE, (player, rect) -> {
            FXGL.inc("hp", -2);
            checkIfDead();
        });
        onCollisionBegin(Entities.PLAYER, Entities.FOOD_OCTAGON, (player, rect) -> {
            FXGL.inc("hp", -5);
            checkIfDead();
        });
    }

    private void checkIfDead() {
        int hp = FXGL.geti("hp");
        if (hp < 1) {
            youDead();
        }
    }

    @Override
    protected void initUI() {
        Button buttonSpeed = new Button();
        newButton(buttonSpeed, "Speed", 50, onUpgradeSpeed);

        Button buttonHp = new Button();
        newButton(buttonHp, "Hp", 80, onUpgradeHp);

        Button buttonBulletSpeed = new Button();
        newButton(buttonBulletSpeed, "Bullet Speed", 110, onUpgradeBulletSpeed);

        Text textScore = new Text();
        textScore.setText("Score: ");
        textScore.setTranslateX((getAppWidth() / 2 + 10));
        textScore.setTranslateY(getAppHeight() - 10);

        Text textHp = new Text("HP: ");
        textHp.setTranslateX(0);
        textHp.setTranslateY(getAppHeight() - 10);

        FXGL.getGameScene().addUINodes(textScore, textHp, buttonSpeed, buttonHp, buttonBulletSpeed);

        textHp.setFont(Font.font(15.0));
        textScore.setFont(Font.font(15.0));

        textScore.textProperty().bind(FXGL.getip("score").asString("Score: %d"));
        textHp.textProperty().bind(FXGL.getip("hp").asString("Hp: %d"));
    }

    private void newButton(Button b, String text, int translateY, EventHandler evt) {
        b.setText("+ " + text);
        b.setTranslateX(0);
        b.setTranslateY(getAppHeight() - translateY);
        b.setOnMouseClicked(evt);
        b.setDisable(true);
    }
    EventHandler onUpgradeSpeed = new EventHandler() {
        @Override
        public void handle(Event t) {
            if (upgradeTokens > 0) {
                int newSpeed = stats.getSpeed();
                newSpeed++;
                stats.setSpeed(newSpeed);
                upgradeTokens--;
            }
        }
    };

    EventHandler onUpgradeHp = new EventHandler() {
        @Override
        public void handle(Event t) {
            if (upgradeTokens > 0) {
                FXGL.inc("hp", 5);
                upgradeTokens--;
            }
        }
    };
    EventHandler onUpgradeBulletSpeed = new EventHandler() {
        @Override
        public void handle(Event t) {
            if (upgradeTokens > 0) {
                Stats instance = Stats.getInstance();
                int newSpeed = instance.getBulletSpeed();
                newSpeed += 50;
                instance.setBulletSpeed(newSpeed);
                upgradeTokens--;
            }
        }
    };

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("hp", 10);
    }

    protected void youDead() {
        System.exit(0);
    }

    @Override
    protected void onUpdate(double tpf) {
        Input input = getInput();
        getGameWorld().getSingleton(Entities.PLAYER).rotateToVector(input.getVectorToMouse(getGameWorld().getSingleton(Entities.PLAYER).getPosition()));

        if (autofire) {
            autofireCount++;
            if (autofireCount > 25) {
                double y = getGameWorld().getSingleton(Entities.PLAYER).getY();
                double x = getGameWorld().getSingleton(Entities.PLAYER).getX();
                spawn("projectile", x - 10, y);
                autofireCount = 0;
            }
        }
        boolean hasToken = (upgradeTokens > 0);

        if (upgradeScore - 10 > 0) {
            upgradeTokens++;
            upgradeScore -= (FXGL.geti("score") / 2);
        }
        FXGL.getGameScene().getUINodes().get(2).setDisable(!hasToken);
        FXGL.getGameScene().getUINodes().get(3).setDisable(!hasToken);
        FXGL.getGameScene().getUINodes().get(4).setDisable(!hasToken);
    }
}
