package albertgame.afengine.in.components;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.IAppLogic;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IGraphicsCreate;
import albertgame.afengine.core.graphics.IGraphicsDraw;
import albertgame.afengine.core.graphics.ITexture;
import albertgame.afengine.core.input.InputServlet;
import albertgame.afengine.core.message.MessageManager;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.in.components.action.ActionComponent;
import albertgame.afengine.in.components.action.SpriteAction;
import albertgame.afengine.in.components.action.TimeAction;
import albertgame.afengine.in.components.render.SceneRenderComponentDraw;
import albertgame.afengine.in.components.render.TextureRenderComponent;
import albertgame.afengine.in.parts.input.InputManager;
import albertgame.afengine.in.parts.input.InputRoute;
import albertgame.afengine.in.parts.scene.Actor;
import albertgame.afengine.in.parts.scene.ActorComponent;
import albertgame.afengine.in.parts.scene.Scene;
import albertgame.afengine.in.parts.scene.SceneManager;

public class ActionComponentTest {
    public static void main(String[] args) {
        DebugUtil.switchOn();
        WindowApp app=new WindowApp(new IAppLogic.AppLogicBase(),"ActionComponentTest","src/test/resources/duke0.gif",800,600);
        App.setInstance(app);
        //添加场景进程，否则无法更新场景内容
        app.getProcessManager().attachProcess(new SceneManager.SceneProcess());
        //添加场景渲染支持，否则无法渲染
        IGraphicsDraw.draw().setRootDrawStrategy(new SceneRenderComponentDraw());
        //添加组件更新逻辑
        ActorComponent.componentMethodList.add(new ActorComponent.
                AdapterProcess(ActionComponent.COMPONENT_NAME));

        //添加输入路由支持，否则无法支持键盘输入
        MessageManager.getInstance().addRoute(new InputRoute());
        initKey();

        //create role
        Actor actor=createRole();

        Scene root=SceneManager.getInstance().getRunningScene();
        root.getRootList().add(actor);

        App.launch();
    }

    private static void initKey(){
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_KeyUp,"bloom",(msg)->{
            int code=(int)msg.extraObjs[0];
            DebugUtil.log("key Enter!");
            if(code==InputServlet.KeyCode_Enter){
                Actor bloom=createBloom();
                SceneManager.getInstance().getRunningScene().getRootList().add(bloom);
                return true;
            }
            return false;
        });
    }

    private static Actor createRole(){
        Actor actor=new Actor("actor1");
        actor.getTransform().position.setX(100).setY(100);
        ITexture texture1= IGraphicsCreate.create().createTexture(ActionComponentTest.class.getClassLoader().getResource("duke0.gif"));
        TextureRenderComponent render=new TextureRenderComponent(texture1);
        actor.addComponent(render,true);
        return actor;
    }

    private static ActionComponent createActionComp1(){
        ActionComponent action=new ActionComponent();
        SpriteAction spriteAction=new SpriteAction("sprite1",true);
        ITexture texture1= IGraphicsCreate.create().createTexture(ActionComponentTest.class.getClassLoader().getResource("duke0.gif"));
        ITexture texture2= IGraphicsCreate.create().createTexture(ActionComponentTest.class.getClassLoader().getResource("duke1.gif"));
        ITexture texture3= IGraphicsCreate.create().createTexture(ActionComponentTest.class.getClassLoader().getResource("duke2.gif"));
        spriteAction.addAnimate(texture1,100).
                addAnimate(texture2,100).
                addAnimate(texture3,100);

        action.addAction(spriteAction);
        return action;
    }
    private static Actor createBloom(){
        Actor bloom=new Actor("bloom");
        bloom.getTransform().position.setX(200).setY(200);
        ActionComponent action=new ActionComponent();

        SpriteAction spriteAction=new SpriteAction("bloom",false);
        spriteAction.setEnd((act)->{
            bloom.dead();
        });
        ITexture texture1=IGraphicsCreate.create().createTexture(ActionComponentTest.class.getClassLoader().getResource("afengine/asset/231/"+1+".png"));
        for(int i=1;i!=12;++i){
            ITexture texture=IGraphicsCreate.create().createTexture(ActionComponentTest.class.getClassLoader().getResource("afengine/asset/231/"+i+".png"));
            spriteAction.addAnimate(texture,200);
        }
        TimeAction timeAction=new TimeAction("move",100,false,()->{
            bloom.getTransform().position.setX(bloom.getTransform().position.getX()+10);
        });

        TextureRenderComponent render=new TextureRenderComponent(texture1);

        action.addAction(spriteAction);
        action.addAction(timeAction);

        bloom.addComponent(action,true);
        bloom.addComponent(render,true);
        return bloom;
    }
}
