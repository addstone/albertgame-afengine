/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.input;

import albertgame.afengine.app.App;
import albertgame.afengine.app.IAppLogic;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.app.message.MessageManager;
import albertgame.afengine.util.DebugUtil;

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
