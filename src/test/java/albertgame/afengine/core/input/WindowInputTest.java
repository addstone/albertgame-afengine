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

/**
 *
 * @author Admin
 */
public class WindowInputTest {
    public static void main(String[] args) {
        DebugUtil.switchOn();//Open Debug

        WindowApp app = new WindowApp(new IAppLogic.AppLogicBase(), "KeyInputTest", "", 800, 600);//instance a window app

        MessageManager.getInstance().addRoute(new InputRoute());//add inputroute to messagemanager
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_KeyUp, InputServlet.ExitServlet);
        
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_WindowActive,"window activet",(msg)->{
            DebugUtil.log("Window Actived!!");
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_MouseInWindow,"in window activet",(msg)->{
            DebugUtil.log("Mouse In Window !!");
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_MouseExitWindow,"Exit window activet",(msg)->{
            DebugUtil.log("Mouse Exit Window !!");
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_WindowDeactive,"Window Deactived",(msg)->{
            DebugUtil.log("Window DeActived!!");
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_WindowDeiconed,"Window Deiconed",(msg)->{
            DebugUtil.log("Window DeIconed!!");
            return false;
        });
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_WindowIconed,"Window Iconed",(msg)->{
            DebugUtil.log("Window Iconed!!");
            return false;
        });
                
        App.launch(app);
    }        
    
}
