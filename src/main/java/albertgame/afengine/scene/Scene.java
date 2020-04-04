/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.scene;

import albertgame.afengine.util.math.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Albert Flex
 */
public class Scene {

    public static interface Loader {
        public abstract void load(Scene scene);
        public abstract void shutdown(Scene scene);
        public abstract void pause(Scene scene);
        public abstract void resume(Scene scene);
    }
    
    private static class AdapterLoader implements Loader{
        @Override
        public void load(Scene scene) {
            System.out.println("Load Scene:"+scene.name);
        }
        @Override
        public void shutdown(Scene scene) {
            System.out.println("Shutdown Scene:"+scene.name);
        }
        @Override
        public void pause(Scene scene) {
            System.out.println("Pause Scene:"+scene.name);
        }
        @Override
        public void resume(Scene scene) {
            System.out.println("Resume Scene:"+scene.name);            
        }        
    };
    
    
    private String name;
    public final Map<String,Actor> nodeActorMap=new HashMap<>();
    private Loader loader;
    private final SceneCamera camera;
    
    public Scene(){
        this("",new AdapterLoader());
    }

    public Scene(String name,Loader loader){
        this.name=name;
        this.loader=loader;
        camera=new SceneCamera(new Vector(0,0,0,0),new Vector(0,0,0,0),0,0);
    }
    public Scene(String name){
        this(name,new AdapterLoader());
    }

    public Loader getLoader() {
        return loader;
    }    

    public SceneCamera getCamera() {
        return camera;
    }
    
    public void setLoader(Loader loader){
        this.loader=loader;
    }
    public final Map<String, Actor> getNodeActorMap() {
        return nodeActorMap;
    }
    
    public final Actor findActorByName(String name){

        Iterator<Actor> actoriter = Actor.staticActorList.iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(name);
            if(dest!=null)
                return dest;
        }        
        
        actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(name);
            if(dest!=null)
                return dest;
        }                
        
        return null;        
    }
    
    public final Actor findActorByID(long id){
        
        Iterator<Actor> actoriter = Actor.staticActorList.iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(id);
            if(dest!=null)
                return dest;
        }        
        
        actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(id);
            if(dest!=null)
                return dest;
        }                
        
        return null;
    }
        
    public final void awakeAllActors(){
        Iterator<Actor> actoriter = Actor.staticActorList.iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            actor.awakeAllComponents();
        }        
        
        actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            if(!actor.isIsStatic())
                actor.awakeAllComponents();
        }                        
    }    
    
    public final void addNodeActor(String nodeName,Actor actor){
        nodeActorMap.put(nodeName, actor);
    }
    public final boolean hasNodeActor(String nodeName){
        return nodeActorMap.containsKey(nodeName);
    }
    public final void removeNodeActor(String nodeName){
        nodeActorMap.remove(nodeName);
    }        
    public String getName() {
        return name;
    }        
    public void setName(String name) {
        this.name = name;
    }
    public void updateScene(long time){
        
        Iterator<Actor> actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            //如果静态实体中存在就不需要更新
            if(!actor.isIsStatic())
                actor.updateActor(time);
        }
    }
}
