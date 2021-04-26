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
import java.awt.TextField;
import java.util.Map;
import javafx.scene.text.Text;

/**
 * author simon kappeler Created At: 26.04.2021
 */
public class Starter extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setIntroEnabled(false);
        settings.setTitle("Diep.io 2.0");
        settings.setWidth(800);
        settings.setHeight(600);
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
            spawn("food_octagon", FXGLMath.random(20, 500), FXGLMath.random(20, 500));
            spawn("food_rectangle", FXGLMath.random(20, 500), FXGLMath.random(20, 500));
            spawn("food_triangle", FXGLMath.random(20, 500), FXGLMath.random(20, 500));
        }

        run(() -> {
            double x = getAppWidth();
            double y = FXGLMath.random(0, getAppHeight() - 20);

        }, Duration.seconds(0.15));

    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(-2));
        onKey(KeyCode.S, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(2));

        onKey(KeyCode.A, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(-2));
        onKey(KeyCode.D, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(2));

        onBtnDown(MouseButton.PRIMARY, () -> {
            double y = getGameWorld().getSingleton(Entities.PLAYER).getY();
            double x = getGameWorld().getSingleton(Entities.PLAYER).getX();
            spawn("projectile", x - 10, y);
            Input input = getInput();
            getGameWorld().getSingleton(Entities.PLAYER).rotateToVector(input.getVectorToMouse(getGameWorld().getSingleton(Entities.PLAYER).getPosition()));
        });
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Entities.PROJECTILE, Entities.FOOD_RECTANGLE, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            spawn("food_rectangle", FXGLMath.random(20, 500), FXGLMath.random(20, 500));
            FXGL.inc("score", +1);
        });
        onCollisionBegin(Entities.PROJECTILE, Entities.FOOD_TRIANGLE, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            spawn("food_triangle", FXGLMath.random(20, 500), FXGLMath.random(20, 500));
            FXGL.inc("score", +1);
        });
        onCollisionBegin(Entities.PLAYER, Entities.FOOD_RECTANGLE, (player, rect) -> {
            FXGL.inc("hp", -1);
            int hp = FXGL.geti("hp");

            if (hp < 1) {
                youDead();
            }
        });
    }

    @Override
    protected void initUI() {
        Text textScore = new Text();
        textScore.setText("Score: ");
        textScore.setTranslateX(0);
        textScore.setTranslateY(getAppHeight() - 10);

        Text textPixels = new Text();
        textPixels.setTranslateX(40);
        textPixels.setTranslateY(getAppHeight() - 10);

        Text textHp = new Text("HP: ");
        textHp.setTranslateX(0);
        textHp.setTranslateY(getAppHeight() - 30);

        Text textHp2 = new Text();
        textHp2.setTranslateX(40);
        textHp2.setTranslateY(getAppHeight() - 30);

        FXGL.getGameScene().addUINode(textPixels);
        FXGL.getGameScene().addUINode(textScore);
        FXGL.getGameScene().addUINode(textHp);
        FXGL.getGameScene().addUINode(textHp2);
        textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("score").asString());
        textHp2.textProperty().bind(FXGL.getWorldProperties().intProperty("hp").asString());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("hp", 10);
    }

    protected void youDead() {
        System.exit(0);
    }
}