/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.in.components;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.IAppLogic;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IGraphicsDraw;
import albertgame.afengine.core.input.InputServlet;
import albertgame.afengine.core.message.MessageManager;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.SoundUtil;
import albertgame.afengine.in.components.behavior.ActorBehavior;
import albertgame.afengine.in.components.behavior.BehaviorBeanComponent;
import albertgame.afengine.in.components.render.SceneRenderComponentDraw;
import albertgame.afengine.in.parts.input.InputManager;
import albertgame.afengine.in.parts.input.InputRoute;
import albertgame.afengine.in.parts.scene.Actor;
import albertgame.afengine.in.parts.scene.ActorComponent;
import albertgame.afengine.in.parts.scene.Scene;
import albertgame.afengine.in.parts.scene.SceneManager;

/**
 *
 * @author Admin
 */
public class BehaviorComponentTest {

    public static void main(String[] args) {
        DebugUtil.switchOn();
        WindowApp app = new WindowApp(new IAppLogic.AppLogicBase(), "ActionComponentTest", "src/test/resources/duke0.gif", 800, 600);
        App.setInstance(app);
        //添加场景进程，否则无法更新场景内容
        app.getProcessManager().attachProcess(new SceneManager.SceneProcess());
        //添加场景渲染支持，否则无法渲染
        IGraphicsDraw.draw().setRootDrawStrategy(new SceneRenderComponentDraw());
        //添加组件更新逻辑，否则组件无法更新
        ActorComponent.componentMethodList.add(new ActorComponent.AdapterProcess(BehaviorBeanComponent.COMPONENT_NAME));

        //添加输入路由支持，否则无法支持键盘输入
        MessageManager.getInstance().addRoute(new InputRoute());
        initKey();

        //create role
        role = createRole();

        Scene root = SceneManager.getInstance().getRunningScene();
        root.getRootList().add(role);

        App.launch();
    }

    private static Actor role;

    public static class Behavior1 extends ActorBehavior {

        @Override
        public void toSleep() {
            DebugUtil.log("sleep");
        }

        @Override
        public void toWake() {
            SoundUtil.get().playSound(sid, 1);
            DebugUtil.log("awake");
        }

        @Override
        public void update(long time) {
        }
    }

    private static long sid = SoundUtil.get().addSound(
            BehaviorComponentTest.class.getClassLoader().getResource("sound1.wav"));

    private static void initKey() {
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_KeyUp, "bloom", (msg) -> {
            int code = (int) msg.extraObjs[0];
            DebugUtil.log("key Enter!");
            if (code == InputServlet.KeyCode_Up) {
                role.awakeAllComponents();
                return true;
            } else if (code == InputServlet.KeyCode_Down) {
                role.sleepAllComponents();
                return true;
            }
            return false;
        });
    }

    private static Actor createRole() {
        Actor actor = new Actor("Role");
        BehaviorBeanComponent bbean = new BehaviorBeanComponent();
        bbean.addBehavior(new Behavior1());
        actor.addComponent(bbean, true);
        return actor;
    }
}
