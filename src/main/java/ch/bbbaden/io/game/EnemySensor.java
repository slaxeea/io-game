package ch.bbbaden.io.game;

import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.Input;
import java.util.function.Predicate;
import javafx.geometry.Point2D;

/**
 * author simon kappeler Created At: 31.05.2021
 */
public class EnemySensor extends Component {

    int count = 0;

    public EnemySensor() {
    }

    public void onUpdate(double tpf) {
        entity.rotateBy(getRotation());

        count++;
        if (getDistance() < 600 && count > 60) {
            // spawn("enemy_projectile", entity.getX(), entity.getY() - 5);
            spawn("enemy_projectile", entity.getX(), entity.getY(), getRotation());
            count = 0;
        }
    }

    public double getDistance() {
        Entity player = FXGL.getGameWorld().getSingleton(Entities.PLAYER);
        double x = Math.abs(entity.getX() - player.getX());
        double y = Math.abs(entity.getY() - player.getY());
        double distance = x + y;
        return distance;
    }

    public float getAngle(Point2D target) {
        double x = entity.getX();
        double y = entity.getY();
        float angle = (float) Math.toDegrees(Math.atan2(target.getY() - y, target.getX() - x));

        angle = (float) (angle + Math.ceil(-angle / 360) * 360);

        return angle;
    }

    public float getRotation() {
        Entity player = FXGL.getGameWorld().getSingleton(Entities.PLAYER);
        Point2D p = (player.getPosition());
        float angle = getAngle(p);
        double rotation = entity.getRotation();

        return (float) (angle - rotation);
    }
}
