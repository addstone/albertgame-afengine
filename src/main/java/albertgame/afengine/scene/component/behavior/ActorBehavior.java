package albertgame.afengine.scene.component.behavior;

//actorbehavior extends actorcomponent,while it's not follow the rule for actorcomponent,but for behavior.

import java.util.HashMap;
import java.util.Map;



//actorbehavior is used to controll the actor
/**
 * Use ActorBehavior for one method your logic code put.<br>
 * you could write a behavior class,and contains some game logic for operate actor,and so on.<br>
 * override the update,or toWake,toSleep<br>
 * as BehaviorBean update ,it will call behavior.update for some logic circle<br>
 * you could sleep this behavior,or wake this behavior.when something is nesscessary.<br>
 * @see BehaviorBeanComponent
 * @author Albert Flex
 */
public abstract class ActorBehavior{
    
    //the key-value is for the file.
    protected BehaviorBeanComponent behaviorbean;
    private String name;
    private boolean active;
    public Map<String,String> attributes;
    
    public ActorBehavior() {
        active=false;
        attributes=new HashMap<>();
    }
    
    public abstract void update(long time);
        
    public void toWake(){
    }        
    
    public void toSleep() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BehaviorBeanComponent getBehaviorbean() {
        return behaviorbean;
    }

    public void setBehaviorbean(BehaviorBeanComponent behaviorbean) {
        this.behaviorbean = behaviorbean;
    }    
}
