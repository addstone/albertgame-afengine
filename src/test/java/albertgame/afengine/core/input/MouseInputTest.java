/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.input;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.IAppLogic;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.message.MessageManager;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.in.parts.input.InputManager;
import albertgame.afengine.in.parts.input.InputRoute;

/**
 *
 * @author Admin
 */
public class MouseInputTest {
    public static void main(String[] args) {
        DebugUtil.switchOn();//Open Debug

        WindowApp app = new WindowApp(new IAppLogic.AppLogicBase(), "KeyInputTest", "", 800, 600);//instance a window app

        MessageManager.getInstance().addRoute(new InputRoute());//add inputroute to messagemanager
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_KeyUp, InputServlet.ExitServlet);
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_MouseDown,"downtest",(msg)->{
            int x =(int)msg.extraObjs[0];
            int y =(int)msg.extraObjs[1];
            int b =(int)msg.extraObjs[2];
            DebugUtil.log("Mouse Down: ["+x+","+y+"]"+","+b);
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_MouseUp,"uptest",(msg)->{
            int x =(int)msg.extraObjs[0];
            int y =(int)msg.extraObjs[1];
            int b =(int)msg.extraObjs[2];
            DebugUtil.log("Mouse Up: ["+x+","+y+"]"+","+b);
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_MouseDrag,"clicktest",(msg)->{
            int x =(int)msg.extraObjs[0];
            int y =(int)msg.extraObjs[1];
//            int b =(int)msg.extraObjs[2];
            DebugUtil.log("Mouse Drag: ["+x+","+y+"]");
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_MouseWheelUp,"wheeluptest",(msg)->{
            DebugUtil.log("Mouse WheelUp!");
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_MouseWheelDown,"wheeldowntest",(msg)->{
            DebugUtil.log("Mouse WheelDown!");
            return false;
        });
                
        App.launch(app);
    }
}
