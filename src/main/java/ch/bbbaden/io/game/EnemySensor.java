package ch.bbbaden.io.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import java.awt.Component;
import javafx.geometry.Point2D;

/**
 * author simon kappeler Created At: 03.05.2021
 */
public class EnemySensor extends Component {

    Entity player = FXGL.getGameWorld().getSingleton(Entities.PLAYER);
    Entity enemy = FXGL.getGameWorld().getSingleton(Entities.ENEMY);

    public EnemySensor() {

    }

    public void onUpdate(double tpf) {
        Point2D pointToPlayer = player.getPosition();
        enemy.translate(pointToPlayer);

    }
}
