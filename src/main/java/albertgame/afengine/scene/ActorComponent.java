package albertgame.afengine.scene;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Albert Flex
 */
public class ActorComponent {

    public final Map<String, String> attributes = new HashMap<>();
    private Actor actor;
    private final String componentName;
    private boolean active;

    public ActorComponent(String compname) {
        this.componentName = compname;
        actor = null;
        active = false;
    }

    public final Actor getActor() {
        return actor;
    }

    public final String getComponentName() {
        return componentName;
    }

    public final boolean isActive() {
        return active;
    }

    public final void awake() {
        if (active) {
            return;
        }
        active = true;
        toWake();
    }

    public final void asleep() {
        if (active == false) {
            return;
        }
        active = false;
        toSleep();
    }

    public final void setActor(Actor actor) {
        this.actor = actor;
    }

    public void update(long time) {
    }

    public void toWake() {
    }

    public void toSleep() {
    }
}
