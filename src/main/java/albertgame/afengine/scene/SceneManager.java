/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.scene;

import albertgame.afengine.util.DebugUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SceneManager {

    private final Map<String, Scene> preparedSceneMap;
    private final Stack<Scene> sceneStack;
    private final Scene rootScene;

    private Scene runningScene;

    public SceneManager(Scene root) {
        sceneStack = new Stack<>();
        preparedSceneMap = new HashMap<>();
        rootScene = root;
        runningScene = root;
    }

    public void prepareScene(Scene scene) {
        if (!preparedSceneMap.containsKey(scene.getName())) {
            preparedSceneMap.put(scene.getName(), scene);
            scene.getLoader().load(scene);
            scene.getLoader().pause(scene);
        } else {
            DebugUtil.error("scene already prepared!");
        }
    }
    
    public void pushScene(Scene scene) {
        if (!sceneStack.contains(scene)) {
            if (!sceneStack.isEmpty()) {
                Scene before = sceneStack.peek();
                before.getLoader().pause(before);
            }
            sceneStack.push(scene);
            scene.getLoader().resume(scene);

            runningScene = scene;
        } else {
            DebugUtil.error("you just add scene before!");
        }
    }

    public void changeScene(Scene scene) {
        if (!sceneStack.contains(scene)) {
            if (!sceneStack.isEmpty()) {
                Scene before = sceneStack.pop();
                before.getLoader().shutdown(before);
            }
            sceneStack.push(scene);
            scene.getLoader().resume(scene);

            runningScene = scene;
        } else {
            DebugUtil.error("you just add scene before!");
        }
    }

    public void popScene() {
        if (!sceneStack.isEmpty()) {
            Scene after = sceneStack.pop();
            after.getLoader().shutdown(after);
        }
        if (!sceneStack.isEmpty()) {
            runningScene = sceneStack.peek();
        } else {
            runningScene = rootScene;
        }
        //最后继续场景的运行
        runningScene.getLoader().resume(runningScene);
    }

    public void update(long time) {
        if (runningScene != null) {
            runningScene.updateScene(time);
        }else{
            DebugUtil.error("runningScene is null!!");
        }
    }
}
