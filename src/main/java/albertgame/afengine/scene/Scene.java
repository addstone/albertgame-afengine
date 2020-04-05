/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.scene;

import albertgame.afengine.scene.ActorComponent.IProcess;
import albertgame.afengine.util.math.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public final List<Actor> rootList=new ArrayList<>();
    private final Loader loader;
    private final SceneCamera camera;
    
    private static final List<Actor> shouldremoves=new ArrayList<>();
    
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

    public List<Actor> getRootList() {
        return rootList;
    }
    
    public SceneCamera getCamera() {
        return camera;
    }
    
    public final Actor findActorByName(String name){

        Iterator<Actor> actoriter = Actor.staticActorList.iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(name);
            if(dest!=null)
                return dest;
        }        
        
        actoriter = rootList.iterator();
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
        
        actoriter = rootList.iterator();
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
        
        actoriter = rootList.iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            if(!actor.isIsStatic())
                actor.awakeAllComponents();
        }                        
    }    
    
    public String getName() {
        return name;
    }        
    public void setName(String name) {
        this.name = name;
    }
    
    public void removeDeadActor(){
        shouldremoves.forEach((a) -> {
            if(rootList.contains(a)){
                rootList.remove(a);
            }else{
                Actor p=a.getParent();
                if(p!=null){
                    p.removeChild(a.id);
                }
            }
            
            //判断静态实体里是否存在，若存在，亦删除
            if(Actor.staticActorList.contains(a)){
                Actor.staticActorList.remove(a);
            }
        });
        shouldremoves.clear();
    }

    public static void setActorDead(Actor actor){
        shouldremoves.add(actor);
    }
    
    public void updateScene(long time){
        
        //移除需要移除的全部实体
        removeDeadActor();
        
        //更新必要的组件进程的操作
        List<IProcess> processlist=ActorComponent.componentMethodList;
        processlist.forEach((process) -> {
            String cname=process.componentName();
            List<ActorComponent> comps=getAllComponentsByName(cname);
            process.process(comps, time);
        });
    }    
    
    public List<ActorComponent> getAllComponentsByName(String name){
        List<ActorComponent> components=new ArrayList<>();
        rootList.forEach((actor)->{
            getComponentsImpl(name,actor,components);
        });
        return components;
    }
    
    private void getComponentsImpl(String name,Actor actor,List<ActorComponent> alls){
        
        ActorComponent comp=actor.getComponent(name);
        if(comp!=null&&comp.isActive()){
            alls.add(comp);
        }
        
        List<Actor> children=actor.getChildren();
        children.forEach((act) -> {
            getComponentsImpl(name,act,alls);
        });
    }
}
