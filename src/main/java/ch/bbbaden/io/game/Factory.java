package ch.bbbaden.io.game;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * author simon kappeler Created At: 26.04.2021
 */
public class Factory implements EntityFactory {

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        var body = new Circle(25, Color.LIGHTBLUE);
        body.setStroke(Color.GRAY);

        var bot = new Rectangle(40, 20, Color.GRAY);
        bot.setStroke(Color.GRAY);
        bot.setTranslateX(20);
        bot.setTranslateY(-10);

        return entityBuilder()
                .type(Entities.PLAYER)
                .from(data)
                .view(body)
                .view(bot)
                .build();
    }

    @Spawns("projectile")
    public Entity newProjectile(SpawnData data) {
        var view = new Circle(15, Color.LIGHTBLUE);
        view.setStroke(Color.WHITE);

        return entityBuilder()
                .type(Entities.PROJECTILE)
                .from(data)
                .viewWithBBox(view)
                .collidable()
                .zIndex(-5)
                .with(new ProjectileComponent(new Point2D(1, 0), 760))
                .build();
    }

    @Spawns("food_rectangle")
    public Entity newFood_rectangle(SpawnData data) {
        var view = new Rectangle(20, 20, Color.YELLOW);
        view.setStroke(Color.GRAY);

        return entityBuilder()
                .type(Entities.FOOD_RECTANGLE)
                .from(data)
                .viewWithBBox(view)
                .collidable()
                .build();
    }
}
