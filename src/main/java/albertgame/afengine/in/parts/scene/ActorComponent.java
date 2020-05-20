package albertgame.afengine.in.parts.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Albert Flex
 */
public class ActorComponent {

    public static final List<IProcess> componentMethodList = new ArrayList<>();

    public static final Map<String, IComponentFactory> factory=new HashMap<>();

    public static void addFactory(String name, IComponentFactory fac) {
        factory.put(name, fac);
    }

    public static IComponentFactory getFactory(String name) {
        return factory.get(name);
    }

    public static interface IProcess {

        public void process(List<ActorComponent> componentlist, long time);

        public String componentName();
    }

    public static class AdapterProcess implements IProcess {

        private final String name;

        public AdapterProcess(String name) {
            this.name = name;
        }

        @Override
        public void process(List<ActorComponent> componentlist, long time) {
            componentlist.forEach((component) -> {
                component.update(time);
            });
        }

        @Override
        public String componentName() {
            return name;
        }
    }

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
