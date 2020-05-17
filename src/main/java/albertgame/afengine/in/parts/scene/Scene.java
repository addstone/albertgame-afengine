/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.in.parts.scene;

import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.math.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Albert Flex
 */
/**
 * 场景配置
 * <scene name="" loader-fac="">
    * <actors>
        * <actor name="" modpath="" mod="a,b,c" or bigactor="">
            * <transform pos="" anchor="" rotate="" scale=""/>
            * <settings>
                * <setting name="" value=""/>
            * </settings>
        * </actor>
        * ...
    * </actors>
    * <map>
        * <actor name="">
            * <actor name=""/>
            * <actor name="">
               * <actor name=""/>
            * </actor>
        * </actor>
    * </map>
 * </scene>
 *
 * 静态实体配置
 * <staticactors>
    * <actors>
        * <actor name="" value=""/>
        * ...
    * </actors>
 * </staticactors>
 *
 * 组件模组配置
 * <modlibs>
    * <mod name="">
        * <component name="">
            * <settings>
            * <setting name="" value=""/>
            * </settings>
        * </component>
    * </mod>
    * <mod .../>
 * </modlibs>
 * @author Albert Flex
 */
public class Scene{

    public static interface Loader {
        public abstract void load(Scene scene);
        public abstract void shutdown(Scene scene);
        public abstract void pause(Scene scene);
        public abstract void resume(Scene scene);
    }

    private static class AdapterLoader implements Loader {
        @Override
        public void load(Scene scene) {
            System.out.println("Load Scene:" + scene.name);
        }
        @Override
        public void shutdown(Scene scene) {
            System.out.println("Shutdown Scene:" + scene.name);
        }
        @Override
        public void pause(Scene scene) {
            System.out.println("Pause Scene:" + scene.name);
        }
        @Override
        public void resume(Scene scene) {
            System.out.println("Resume Scene:" + scene.name);
        }
    };

    private String name;
    public final List<Actor> rootList = new ArrayList<>();
    private Loader loader;
    private SceneCamera camera;
    
    //实体里最好存放管理游戏生命周期的实体，不要和放到场景里，否则会出现组件更新两次
    private static final List<Actor> shouldremoves = new ArrayList<>();

    public Scene() {
        this("", new AdapterLoader());
    }

    public Scene(String name, Loader loader) {
        this.name = name;
        this.loader = loader;
        camera = new SceneCamera(new Vector(0, 0, 0, 0), new Vector(0, 0, 0, 0), 0, 0);
    }

    public Scene(String name) {
        this(name, new AdapterLoader());
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

    public void setCamera(SceneCamera camera) {
        this.camera = camera;
    }

    public final Actor findActorByName(String name) {

        Iterator<Actor> actoriter = Actor.staticActorList.iterator();
        while (actoriter.hasNext()) {
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(name);
            if (dest != null) {
                return dest;
            }
        }

        actoriter = rootList.iterator();
        while (actoriter.hasNext()) {
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(name);
            if (dest != null) {
                return dest;
            }
        }

        return null;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public final Actor findActorByID(long id) {

        Iterator<Actor> actoriter = Actor.staticActorList.iterator();
        while (actoriter.hasNext()) {
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(id);
            if (dest != null) {
                return dest;
            }
        }

        actoriter = rootList.iterator();
        while (actoriter.hasNext()) {
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(id);
            if (dest != null) {
                return dest;
            }
        }

        return null;
    }

    public final void awakeAllActors() {

        //唤醒静态
        Iterator<Actor> actoriter = Actor.staticActorList.iterator();
        while (actoriter.hasNext()) {
            Actor actor = actoriter.next();
            actor.awakeAllComponents();
        }

        actoriter = rootList.iterator();
        while (actoriter.hasNext()) {
            Actor actor = actoriter.next();
            actor.awakeAllComponents();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeDeadActor() {
        shouldremoves.forEach((a) -> {
            if (rootList.contains(a)) {
                DebugUtil.log("remove dead actor:"+a.getName());
                rootList.remove(a);
            } else {
                Actor p = a.getParent();
                if (p != null) {
                    p.removeChild(a.id);
                }
            }

            //判断静态实体里是否存在，若存在，亦删除
            if (Actor.staticActorList.contains(a)) {
                Actor.staticActorList.remove(a);
            }
        });
        shouldremoves.clear();
    }

    public static void setActorDead(Actor actor) {
        shouldremoves.add(actor);
    }

    public void updateScene(long time) {

        //移除需要移除的全部实体
        removeDeadActor();

        //更新必要的组件进程的操作
        List<ActorComponent.IProcess> processlist = ActorComponent.componentMethodList;
        processlist.forEach((process) -> {
            String cname = process.componentName();
            List<ActorComponent> comps = getAllComponentsByName(cname);
            process.process(comps, time);
        });
    }

    public List<ActorComponent> getAllComponentsByName(String name) {
        List<ActorComponent> components = new ArrayList<>();
        Actor.staticActorList.forEach((actor) -> {
            getComponentsImpl(name, actor, components);
        });
        rootList.forEach((actor) -> {
            getComponentsImpl(name, actor, components);
        });
        return components;
    }

    private void getComponentsImpl(String name, Actor actor, List<ActorComponent> alls) {

        ActorComponent comp = actor.getComponent(name);
        if (comp != null && comp.isActive()) {
            alls.add(comp);
        }

        List<Actor> children = actor.getChildren();
        children.forEach((act) -> {
            getComponentsImpl(name, act, alls);
        });
    }
}
