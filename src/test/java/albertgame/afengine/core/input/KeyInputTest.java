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
public class KeyInputTest {

    public static void main(String[] args) {
        DebugUtil.switchOn();//Open Debug

        WindowApp app = new WindowApp(new IAppLogic.AppLogicBase(), "KeyInputTest", "", 800, 600);//instance a window app

        MessageManager.getInstance().addRoute(new InputRoute());//add inputroute to messagemanager
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_KeyUp, InputServlet.ExitServlet);
        InputManager.getInstance().addAfterServlet(InputServlet.EventCode_KeyUp,
                "KeyUp1", (msg) -> {
                    int keycode = (int) ((Integer) msg.extraObjs[0]);
                    DebugUtil.log("key up1 " + (char) keycode + " [" + keycode + "]");
                    if(keycode==InputServlet.CharCode('E')){
                        return true;
                    }
                    return false;
                });
        
        InputManager.getInstance().addAfterServlet(InputServlet.EventCode_KeyUp,
                "KeyUp2", (msg) -> {
                    int keycode = (int) ((Integer) msg.extraObjs[0]);
                    DebugUtil.log("key up2 " + (char) keycode + " [" + keycode + "]");
                    return false;
                });
        InputManager.getInstance().addAfterServlet(InputServlet.EventCode_KeyType, "type",
                (msg) -> {
                    char text = (char) msg.extraObjs[0];
                    DebugUtil.log("key type [" + text + "]");
                    return true;
                });

        App.launch(app);
    }
}
