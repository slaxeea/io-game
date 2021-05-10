package ch.bbbaden.io.game;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.FollowComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * author simon kappeler Created At: 26.04.2021
 */
public class Factory implements EntityFactory {

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        var body = new Circle(25, Color.LIGHTBLUE);
        body.setStroke(Color.GRAY);

        var bot = new Rectangle(10, 20, Color.GRAY);
        bot.setStroke(Color.GRAY);
        bot.setTranslateX(20);
        bot.setTranslateY(-10);

        return entityBuilder()
                .type(Entities.PLAYER)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .viewWithBBox(body)
                .viewWithBBox(bot)
                .collidable()
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        var body = new Circle(25, Color.RED);
        body.setStroke(Color.GRAY);

        var bot = new Rectangle(10, 20, Color.GRAY);
        bot.setStroke(Color.GRAY);
        bot.setTranslateX(20);
        bot.setTranslateY(-10);
        Stats stats = Stats.getInstance();

        return entityBuilder()
                .type(Entities.ENEMY)
                .from(data)
                .with(new FollowComponent(FXGL.getGameWorld().getSingleton(Entities.PLAYER), 50, 0, 0))
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .viewWithBBox(body)
                .viewWithBBox(bot)
                .collidable()
                .build();
    }

    @Spawns("projectile")
    public Entity newProjectile(SpawnData data) {
        Input input = getInput();
        var view = new Circle(10, Color.LIGHTBLUE);
        view.setStroke(Color.GRAY);
        int bulletSpeed = Stats.getInstance().getBulletSpeed();
        return entityBuilder()
                .type(Entities.PROJECTILE)
                .from(data)
                .viewWithBBox(view)
                .collidable()
                .with(new ProjectileComponent(input.getVectorToMouse(getGameWorld().getSingleton(Entities.PLAYER).getPosition()), bulletSpeed))
                .with(new OffscreenCleanComponent())
                .bbox(new HitBox(BoundingShape.box(10, 10)))
                .zIndex(-10)
                .build();
    }

    @Spawns("food_rectangle")
    public Entity newFood_rectangle(SpawnData data) {
        var view = new Rectangle(30, 30, Color.YELLOW);
        view.setStroke(Color.GRAY);

        return entityBuilder()
                .type(Entities.FOOD_RECTANGLE)
                .from(data)
                .zIndex(-2)
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .viewWithBBox(view)
                .collidable()
                .build();
    }

    @Spawns("food_triangle")
    public Entity newFood_triangle(SpawnData data) {
        var view = new Rectangle(30, 30, Color.RED);
        view.setStroke(Color.GRAY);

        return entityBuilder()
                .type(Entities.FOOD_TRIANGLE)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .zIndex(-2)
                .viewWithBBox(view)
                .collidable()
                .build();
    }

    @Spawns("food_octagon")
    public Entity newFood_octagon(SpawnData data) {
        var view = new Rectangle(30, 30, Color.PURPLE);
        var view2 = new Rectangle(30, 30, Color.PURPLE);
        view.setStroke(Color.GRAY);
        view2.setStroke(Color.GRAY);
        view2.setRotate(45);

        return entityBuilder()
                .type(Entities.FOOD_OCTAGON)
                .from(data)
                .zIndex(-2)
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .viewWithBBox(view)
                .viewWithBBox(view2)
                .collidable()
                .build();
    }
}
