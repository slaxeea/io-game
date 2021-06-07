package ch.bbbaden.io.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.InputSequence;
import com.almasb.fxgl.input.UserAction;
import java.util.Map;
import java.util.Random;

/**
 * author simon kappeler Created At: 26.04.2021
 */
public class Starter extends GameApplication {

    private boolean autofire = false;
    private int autofireCount = 0;
    private boolean amg = false;
    private Stats stats = Stats.getInstance();
    int fps;
    long lastTime;

    @Override
    // Initialize the Settings
    protected void initSettings(GameSettings settings) {
        settings.setIntroEnabled(false);
        settings.setTitle("Tank Gaem");
        settings.setFullScreenAllowed(true);
        settings.setHeight(stats.getHeight());
        settings.setWidth(stats.getWidth());

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    // Initialize the game and spawn stuff
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.WHITE);
        getGameWorld().addEntityFactory(new Factory());

        spawn("player", getAppWidth() / 2, getAppHeight() / 2 - 30);
        stats.spawnFood("enemy");
        stats.spawnFood("enemy");
        stats.spawnFood("spikey");
        stats.randomFood();
    }

    @Override
    // Initialize player input
    protected void initInput() {
        Input input = FXGL.getInput();
        // Konami code for surprise ;)
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
        // Move forward
        onKey(KeyCode.W, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(-stats.getSpeed()));
        // Move backward
        onKey(KeyCode.S, () -> getGameWorld().getSingleton(Entities.PLAYER).translateY(stats.getSpeed()));
        // Go left
        onKey(KeyCode.A, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(-stats.getSpeed()));
        // Go right
        onKey(KeyCode.D, () -> getGameWorld().getSingleton(Entities.PLAYER).translateX(stats.getSpeed()));
        // Turn on autofire (a bit overpowered though)
        onKey(KeyCode.O, () -> this.autofire = this.autofire ? false : true);

        // shoot!
        onBtnDown(MouseButton.PRIMARY, () -> {
            double y = stats.getPlayer().getY() + 10;
            double x = stats.getPlayer().getX() + 10;
            spawn("projectile", x, y);
        });
    }

    @Override
    // Initialize the collision Events from CollisionEvents.java
    protected void initPhysics() {
        new CollisionEvents().initphysics();
    }

    @Override
    // Make the GUI
    protected void initUI() {
        new GUI();
    }

    @Override
    // Initialize the Variables which we want FXGL to update itself
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("upgradeTokens", 0);
        vars.put("score", 0);
        vars.put("hp", 10);
        vars.put("fps", 0);
    }

    @Override
    // Main game loop
    protected void onUpdate(double tpf) {
        // to calculate frames per second
        lastTime = System.nanoTime();
        Input input = getInput();
        // rotate the player to the position of the mouse
        getGameWorld().getSingleton(Entities.PLAYER).rotateToVector(input.getVectorToMouse(getGameWorld().getSingleton(Entities.PLAYER).getPosition()));

        // Autofire if player pressed 'O'
        if (autofire) {
            autofireCount++;
            if (autofireCount > stats.getReload()) {
                // fire from the position of the player
                double y = getGameWorld().getSingleton(Entities.PLAYER).getY();
                double x = getGameWorld().getSingleton(Entities.PLAYER).getX();
                spawn("projectile", x - 10, y);
                autofireCount = 0;
            }
        }

        // Update UpgradeScore and UpgradeTokens, display 'em
        if (stats.getUpgradeScore() > 10) {
            stats.incUpgradeToken(1);
            FXGL.inc("upgradeTokens", 1);
            stats.incUpgradeScore(-(FXGL.geti("score") / 2));
        }

        // Disable upgrade buttons if player has no tokens
        boolean hasToken = (stats.getUpgradeTokens() > 0);
        for (int i = 0; i < FXGL.getGameScene().getUINodes().toArray().length - 3; i++) {
            FXGL.getGameScene().getUINodes().get(i).setDisable(!hasToken);
        }

        // Make the center of the screen to be the player 
        Viewport viewport = getGameScene().getViewport();
        int x = (int) stats.getPlayer().getX();
        int y = (int) stats.getPlayer().getY();
        viewport.setX(x - (stats.getWidth()) / 2);
        viewport.setY(y - (stats.getHeight()) / 2);

        // Spawn new random enemies to make the game harder the longer one plays
        Random r = new Random();
        if (r.nextInt(10000) == 2) {
            stats.spawnFood("enemy");
        }

        // Calculate frames per seconds cus i was bored
        fps = (int) (1000000000 / (System.nanoTime() - lastTime));
        lastTime = System.nanoTime();
        // System.out.println("fps: " + fps);
    }
}
