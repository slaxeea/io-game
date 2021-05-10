
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.entity.component.Component
import kotlin.math.abs

class EnemySensor : Component() {

    private var prevX = 0.0
    private var prevY = 0.0

    override fun onAdded() {
        prevX = entity.x
        prevY = entity.y
    }

    override fun onUpdate(tpf: Double) {
        Entity player = FXGL.getGameWorld().getSingleton(Entities.PLAYER);
        entity.rotateToVector(
        Point2D pointToPlayer = player.getPosition());
    }

    override fun isComponentInjectionRequired(): Boolean = false
}