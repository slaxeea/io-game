package ch.bbbaden.io.game;

import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

/**
 * author simon kappeler Created At: 31.05.2021
 */
// Makes the enemy aim at the player and shoot if he's close enough
public class EnemySensor extends Component {

    int count = 0;

    public EnemySensor() {
    }

    public void onUpdate(double tpf) {
        entity.rotateBy(getRotation());

        count++;
        // Shoot if the player is close
        if (getDistance() < 600 && count > 60) {
            // spawn("enemy_projectile", entity.getX(), entity.getY() - 5);
            spawn("enemy_projectile", entity.getX(), entity.getY(), getRotation());
            count = 0;
        }
    }

    // returns the difference between enemy position and player position
    public double getDistance() {
        Entity player = FXGL.getGameWorld().getSingleton(Entities.PLAYER);
        double x = Math.abs(entity.getX() - player.getX());
        double y = Math.abs(entity.getY() - player.getY());
        double distance = x + y;
        return distance;
    }

    // returns the angel between enemy and player
    public float getAngle(Point2D target) {
        double x = entity.getX();
        double y = entity.getY();
        float angle = (float) Math.toDegrees(Math.atan2(target.getY() - y, target.getX() - x));

        angle = (float) (angle + Math.ceil(-angle / 360) * 360);

        return angle;
    }

    // returns the rotation the enemy needs to be at, to shoot the player
    public float getRotation() {
        Entity player = FXGL.getGameWorld().getSingleton(Entities.PLAYER);
        Point2D p = (player.getPosition());
        float angle = getAngle(p);
        double rotation = entity.getRotation();

        return (float) (angle - rotation);
    }
}
