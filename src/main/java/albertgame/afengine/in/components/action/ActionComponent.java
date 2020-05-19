package albertgame.afengine.in.components.action;

import albertgame.afengine.in.parts.scene.ActorComponent;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.math.IDGenerator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Albert Flex
 */
public class ActionComponent extends ActorComponent{
    
    public static final String COMPONENT_NAME="Action";
    //the action will send message when action is start,or end.
    public static final long MESSAGE_TYPE_ACTION=IDGenerator.createId();

    public static final long ACTION_START=IDGenerator.createId();
    public static final long ACTION_END=IDGenerator.createId();

    public ActionComponent() {
        super(ActionComponent.COMPONENT_NAME);
        actionMap=new HashMap<>();
    }

    public static interface ActionStart {
        public void start(ActAction action);
    }
    public static interface ActionEnd{
        public void end(ActAction action);
    }
    
    //use action linkedlist
    public static abstract class ActAction{
        private ActionStart start;
        private ActionEnd end;

        protected ActionComponent actionComp;
        protected String actionName;
        protected ActAction nextAction;
        public ActAction(String name){
            this.actionName=name;
            nextAction=null;
        }
        public void doStart(){
            start();
            if(start!=null){
                start.start(this);
            }
        }
        public void start(){}
        public void doEnd(){
            end();
            if(end!=null){
                end.end(this);
            }
        }
        public void end(){}
        public void handle(String cmd){}

        public void setStart(ActionStart start) {
            this.start = start;
        }

        public void setEnd(ActionEnd end) {
            this.end = end;
        }

        public abstract boolean isEnd();
        public abstract void update(long time);
        public void setNextAction(ActAction action){
            nextAction=action;
            action.actionComp=this.actionComp;
        }
        public ActAction getNextAction(){
            return nextAction;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public ActionComponent getActionComp() {
            return actionComp;
        } 
    }
    
    private final Map<String,ActAction> actionMap;
    public void addAction(ActAction action){
        this.actionMap.put(action.actionName, action);
        if(action.actionComp!=null){
            action.actionComp.removeAction(action.actionName);
        }
        DebugUtil.log("->"+action.actionName);
        action.actionComp=this;
        ActAction ac = action.nextAction;
        action.doStart();
        while(ac!=null){
            ac.actionComp=this;
            DebugUtil.log("->>"+ac.actionName);
            ac=ac.nextAction;
        }
    }
    public void removeAction(String name){
        ActAction action=this.actionMap.remove(name);
        action.end();
        action.actionComp=null;
    }
    public ActAction  getAction(String name){
        return actionMap.get(name);
    }

    public Map<String, ActAction> getActionMap() {
        return actionMap;
    }
    public void missionCommand(String actionname,String command){
        ActAction action = actionMap.get(actionname);
        if(action==null){
            DebugUtil.log("no actio for "+action+" found.");
        }else{
            action.handle(command);
        }
    }

    List<ActAction> shoulddeletelist = new LinkedList<>();
    @Override
    public void update(long time) {
        Iterator<ActAction> actioniter = actionMap.values().iterator();
        while(actioniter.hasNext()){
            ActAction action= actioniter.next();            
            if(action.isEnd()){
                shoulddeletelist.add(action);                
            }            
            action.update(time);
        }
        
        actioniter = shoulddeletelist.iterator();
        while(actioniter.hasNext()){
            ActAction action =actioniter.next();
            action.doEnd();
            actionMap.remove(action.actionName);            
            if(action.getNextAction()!=null){
                ActAction act = action.nextAction;
                act.doStart();
                actionMap.put(act.actionName,act);
                DebugUtil.log("to - "+act.actionName);
            }
        }
        
        shoulddeletelist.clear();
    }            
}
