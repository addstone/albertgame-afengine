/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.scene;

import albertgame.afengine.util.DebugUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Center of Scene<br>
 * manager scene lifespan<br>
 * add scene to SceneCenter <br>
 * provide some methods for scene manager<br>
 * if you just want add scene for prepare not show,you could call prepareScene<br>
 * if you want push scene,you could return to previous Scene,you should use pushScene(scene),<br>
 * if you want push scene,you sure do not return to previous Scene,use changeToScene(scene),<br>
 * if you want return to lastscene,you should use popScene,<br>
 * if you want to return to rootScene,use poptoScene,<br>
 * while you should push a scene as a rootScene when you use up methods<br>
 * @see Scene
 * @author Albert Flex
 */
public class SceneManager {
    
    private final Map<String,Scene> preparedSceneMap=new HashMap<>();
    private final Stack<Scene> sceneStack;    
    private Scene runningScene;
    private Scene rootScene;

    public SceneManager(){
        sceneStack=new Stack<>();
    }

    public void pushScene(Scene scene){           
        
        //如果场景栈中存在场景，就不需要切换
        if(runningScene==scene||stackContainsScene(scene.getName()))
            return;

        if(rootScene==null){
            rootScene=scene;            
            runningScene=rootScene;
            runningScene.getLoader().load(runningScene);
            return;
        }
        if(runningScene==rootScene&&runningScene!=null&&rootScene!=null){
            runningScene.getLoader().pause(runningScene);            
            runningScene=scene;
            scene.getLoader().load(scene);
            return;
        }
        if(runningScene!=null&&runningScene!=rootScene){
            runningScene.getLoader().pause(runningScene);
            sceneStack.push(runningScene);            
            scene.getLoader().load(scene);
            runningScene=scene;
        }
    }
    public void popScene(){        
        if(runningScene==rootScene){
            return;
        }

        runningScene.getLoader().shutdown(runningScene);
        if(sceneStack.isEmpty())
        {
            if(rootScene!=null){
                runningScene=rootScene;
            }
            else{
                DebugUtil.log(DebugUtil.LogType.ERROR,"There is no scene to run!");
                runningScene=null;
            }
        }
        else{
            runningScene=sceneStack.pop();
        }

        if(runningScene!=null)
            runningScene.getLoader().resume(runningScene);
    }
    public void popToRoot(){        
        if(rootScene==null||runningScene==null){
            return;
        }

        if(rootScene==runningScene){
            return;
        }

        runningScene.getLoader().shutdown(runningScene);

        if(!sceneStack.isEmpty())
        {
            Scene scene = sceneStack.pop();
            scene.getLoader().shutdown(scene);
        }

        sceneStack.clear();

        runningScene=rootScene;       
        if(runningScene!=null){
            runningScene.getLoader().resume(runningScene);
        }
    }
    private boolean stackContainsScene(String name){
        Iterator<Scene> sceneiter = sceneStack.iterator();
        while(sceneiter.hasNext()){
            Scene scene = sceneiter.next();
            if(scene.getName().equals(name))
                return true;
        }
        return false;
    }
    public void changeToScene(Scene scene){
        //如果场景栈中存在场景，就不需要切换
        if(stackContainsScene(scene.getName()))
            return;

        if(rootScene!=null&&runningScene!=rootScene){
            runningScene.getLoader().shutdown(runningScene);            
        }
        else if(rootScene==null&&runningScene==null){
            rootScene=scene;
        }

        scene.getLoader().load(scene);
        runningScene=scene;
    }

    public Map<String, Scene> getPreparedSceneMap() {
        return preparedSceneMap;
    }

    public Stack<Scene> getSceneStack() {
        return sceneStack;
    }

    public Scene getRootScene() {
        return rootScene;
    }
    
    public Scene getRunningScene(){
        return runningScene;
    }
    public void update(long time){   
        Actor.updateStaticActor(time);
        
        if(runningScene!=null){
            runningScene.updateScene(time);
        }
        else{
            if(rootScene!=null){
                runningScene=rootScene;
                rootScene.getLoader().resume(rootScene);
                rootScene.updateScene(time);
            }
        }
    }
    
    
    public String prepareScene(Scene scene){
        //如果已经在运行的，就不需要准备了
        if(stackContainsScene(scene.getName()))
            return null;
        
        if(!preparedSceneMap.containsKey(scene.getName())){
            preparedSceneMap.put(scene.getName(), scene);
            scene.getLoader().load(scene);
            scene.getLoader().pause(scene);
            return scene.getName();
        }
        else{
            DebugUtil.log(DebugUtil.LogType.ERROR,"prepared scene should not add again!");
            return null;
        }
    }
    
    public void pushPreparedScene(String name){
        Scene scene = preparedSceneMap.get(name);
        if(scene!=null){
            pushScene(scene);
        }
        else{
            DebugUtil.log(DebugUtil.LogType.ERROR,"no prepared Scene Found:"+name);
        }
    }
    
    public Scene findPreparedScene(String name){
        return preparedSceneMap.get(name);
    }    
    
}
