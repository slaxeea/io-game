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
import com.almasb.fxgl.input.InputSequence;
import com.almasb.fxgl.input.UserAction;
import java.util.Map;
import javafx.geometry.Rectangle2D;

/**
 * author simon kappeler Created At: 26.04.2021
 */
public class Starter extends GameApplication {

    private boolean autofire = false;
    private int autofireCount = 0;
    private boolean amg = false;
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
        stats.spawnFood("enemy");
        stats.spawnFood("enemy");

        stats.randomFood();
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        var sequence = new InputSequence(KeyCode.W, KeyCode.W, KeyCode.S, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.A, KeyCode.D);
        input.addAction(new UserAction("amogus") {
            @Override
            protected void onAction() {
                if (!amg) {
                    stats.spawnFood("easteregg");
                    amg = true;
                }
            }
         ;
        }, sequence);
        onKey(KeyCode.W, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(-stats.getSpeed()));
        onKey(KeyCode.S, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(stats.getSpeed()));

        onKey(KeyCode.A, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(-stats.getSpeed()));
        onKey(KeyCode.D, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(stats.getSpeed()));

        onKey(KeyCode.O, () -> this.autofire = this.autofire ? false : true);

        onBtnDown(MouseButton.PRIMARY, () -> {
            double y = stats.getPlayer().getY() + 10;
            double x = stats.getPlayer().getX() + 10;
            spawn("projectile", x, y);
        });
    }

    @Override

    protected void initPhysics() {
        new CollisionEvents().initphysics();
    }

    @Override
    protected void initUI() {
        new GUI();
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

        if (stats.getUpgradeScore() > 10) {
            stats.incUpgradeToken(1);
            FXGL.inc("upgradeTokens", 1);
            stats.incUpgradeScore(-(FXGL.geti("score") / 2));
        }

        boolean hasToken = (stats.getUpgradeTokens() > 0);
        for (int i = 0; i < FXGL.getGameScene().getUINodes().toArray().length - 3; i++) {
            FXGL.getGameScene().getUINodes().get(i).setDisable(!hasToken);
        }

        Viewport viewport = getGameScene().getViewport();
        int x = (int) stats.getPlayer().getX();
        int y = (int) stats.getPlayer().getY();
        viewport.setX(x - (stats.getWidth()) / 2);
        viewport.setY(y - (stats.getHeight()) / 2);
    }
}
