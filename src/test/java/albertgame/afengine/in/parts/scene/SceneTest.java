/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.in.parts.scene;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.IAppLogic;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IGraphicsCreate;
import albertgame.afengine.core.graphics.IGraphicsDraw;
import albertgame.afengine.core.graphics.ITexture;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.in.components.action.ActionComponent;
import albertgame.afengine.in.components.behavior.BehaviorBeanComponent;
import albertgame.afengine.in.components.render.SceneRenderComponentDraw;
import albertgame.afengine.in.components.render.TextureRenderComponent;

/**
 *
 * @author Admin
 */
public class SceneTest {
    public static void main(String[] args) {
        DebugUtil.switchOn();
        WindowApp app=new WindowApp(new IAppLogic.AppLogicBase(),"ActionComponentTest","src/test/resources/duke0.gif",800,600);
        App.setInstance(app);
        //添加场景进程，否则无法更新场景内容
        app.getProcessManager().attachProcess(new SceneManager.SceneProcess());
        //添加场景渲染支持，否则无法渲染
        IGraphicsDraw.draw().setRootDrawStrategy(new SceneRenderComponentDraw());
        //添加组件更新逻辑，否则组件无法更新
        ActorComponent.componentMethodList.add(new ActorComponent.
                AdapterProcess(ActionComponent.COMPONENT_NAME));
        ActorComponent.componentMethodList.add(new ActorComponent.
                AdapterProcess(BehaviorBeanComponent.COMPONENT_NAME));
        
        //create role
        Actor actor=createRoot();

        Scene root=SceneManager.getInstance().getRunningScene();
        root.getRootList().add(actor);

        App.launch();
    }

    private static Actor createRoot() {
        String url="afengine/asset/231/1.png";
        Actor actor=createSprite("root",0,0,url);
        Actor node1=createSprite("node1",50,50,url);
        Actor node2=createSprite("node2",50,50,url);
        Actor node3=createSprite("node3",50,50,url);
        Actor node4=createSprite("node4",0,50,url);
        
        DebugUtil.log("actor pos:"+actor.getAbsoluteX()+","+actor.getAbsoluteY());
        node3.addChild(node4);
        node2.addChild(node3);
        node1.addChild(node2);
        actor.addChild(node1);
        return actor;
    }
    private static Actor createSprite(String name,int x,int y,String imgurl){
        Actor actor=new Actor(name);
        actor.getTransform().position.setX(x).setY(y);
        ITexture texture1= IGraphicsCreate.create().createTexture(SceneTest.class.getClassLoader().getResource(imgurl));
        TextureRenderComponent render=new TextureRenderComponent(texture1);
        actor.addComponent(render,true);        
        
        return actor;
    }
}
