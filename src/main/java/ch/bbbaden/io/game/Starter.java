package ch.bbbaden.io.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.input.Input;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

import javafx.scene.control.Button;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * author simon kappeler Created At: 26.04.2021
 */
public class Starter extends GameApplication {

    private boolean autofire = false;
    private int autofireCount = 0;
    private Stats stats = Stats.getInstance();
    private EventHandlers evt = EventHandlers.getInstance();

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
        stats.spawnFood("enemy");
        stats.spawnFood("enemy");

        for (int i = 0; i < FXGLMath.random(2, 5); i++) {
            stats.spawnFood("food_rectangle");
        }
        for (int i = 0; i < FXGLMath.random(1, 4); i++) {
            stats.spawnFood("food_triangle");
        }
        for (int i = 0; i < FXGLMath.random(1, 3); i++) {
            stats.spawnFood("food_octagon");
        }
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(-stats.getSpeed()));
        onKey(KeyCode.S, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(stats.getSpeed()));

        onKey(KeyCode.A, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(-stats.getSpeed()));
        onKey(KeyCode.D, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(stats.getSpeed()));

        onKey(KeyCode.O, () -> this.autofire = this.autofire ? false : true);

        onBtnDown(MouseButton.PRIMARY, () -> {
            double y = getGameWorld().getSingleton(Entities.PLAYER).getY();
            double x = getGameWorld().getSingleton(Entities.PLAYER).getX();
            spawn("projectile", x - 10, y);
        });
    }

    @Override
    protected void initPhysics() {
        new CollisionEvents().initphysics();
    }

    @Override
    protected void initUI() {
        Button buttonSpeed = new Button();
        newButton(buttonSpeed, "Speed", 50, evt.getOnUpgradeSpeed());

        Button buttonHp = new Button();
        newButton(buttonHp, "Hp", 80, evt.getOnUpgradeHp());

        Button buttonBulletSpeed = new Button();
        newButton(buttonBulletSpeed, "Bullet Speed", 110, evt.getOnUpgradeBulletSpeed());

        Button buttonReload = new Button();
        newButton(buttonReload, "Reload Speed", 140, evt.getOnUpgradeReload());

        Button buttonRegen = new Button();
        newButton(buttonRegen, "HP Regen", 170, evt.getOnUpgradeRegen());

        Text textScore = new Text();
        textScore.setText("Score: ");
        textScore.setTranslateX((getAppWidth() / 2 + 10));
        textScore.setTranslateY(getAppHeight() - 10);

        Text textHp = new Text("HP: ");
        textHp.setTranslateX(0);
        textHp.setTranslateY(getAppHeight() - 10);

        Text textUpgrade = new Text("Upgrade Tokens: ");
        textUpgrade.setTranslateX(0);
        textUpgrade.setTranslateY(20);

        FXGL.getGameScene().addUINodes(textScore, textHp, textUpgrade);

        textHp.setFont(Font.font(15.0));
        textScore.setFont(Font.font(15.0));

        textUpgrade.textProperty().bind(FXGL.getip("upgradeTokens").asString("Upgrade Tokens: %d"));
        textScore.textProperty().bind(FXGL.getip("score").asString("Score: %d"));
        textHp.textProperty().bind(FXGL.getip("hp").asString("Hp: %d"));
    }

    private void newButton(Button b, String text, int translateY, EventHandler evth) {
        b.setText("+ " + text);
        b.setTranslateX(0);
        b.setTranslateY(getAppHeight() - translateY - 10);
        b.setOnMouseClicked(evth);
        b.setDisable(true);
        b.setMinWidth(100);
        b.setAlignment(Pos.CENTER_LEFT);

        FXGL.getGameScene().addUINode(b);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("upgradeTokens", 0);
        vars.put("score", 0);
        vars.put("hp", 10);
    }

    @Override
    protected void onUpdate(double tpf) {
        Input input = getInput();
        getGameWorld().getSingleton(Entities.PLAYER).rotateToVector(input.getVectorToMouse(getGameWorld().getSingleton(Entities.PLAYER).getPosition()));

        if (autofire) {
            autofireCount++;
            if (autofireCount > stats.getReload()) {
                double y = getGameWorld().getSingleton(Entities.PLAYER).getY();
                double x = getGameWorld().getSingleton(Entities.PLAYER).getX();
                spawn("projectile", x - 10, y);
                autofireCount = 0;
            }
        }

        if (stats.getUpgradeScore() - 10 > 0) {
            stats.incUpgradeToken(1);
            FXGL.inc("upgradeTokens", 1);
            stats.incUpgradeScore(-(FXGL.geti("score") / 2));
        }

        boolean hasToken = (stats.getUpgradeTokens() > 0);
        for (int i = 0; i < FXGL.getGameScene().getUINodes().toArray().length - 2; i++) {
            FXGL.getGameScene().getUINodes().get(i).setDisable(!hasToken);
        }

        Viewport viewport = getGameScene().getViewport();
        int x = (int) stats.getPlayer().getX();
        int y = (int) stats.getPlayer().getY();
        viewport.setX(x - (stats.getWidth()) / 2);
        viewport.setY(y - (stats.getHeight()) / 2);
    }
}
