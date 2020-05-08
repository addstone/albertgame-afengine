/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.input.ui;

import albertgame.afengine.app.App;
import albertgame.afengine.app.IAppLogic;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.app.message.MessageManager;
import albertgame.afengine.graphics.IColor;
import albertgame.afengine.graphics.IFont;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.input.InputManager;
import albertgame.afengine.input.InputRoute;
import albertgame.afengine.input.UIFace;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.math.Vector;
import albertgame.afengine.util.property.StringProperty;

/**
 *
 * @author Admin
 */
public class ButtonTest {
    public static void main(String[] args) {
        DebugUtil.switchOn();//Open Debug

        WindowApp app = new WindowApp(new IAppLogic.AppLogicBase(), "KeyInputTest", "", 800, 600);//instance a window app
        App.setInstance(app);

        MessageManager.getInstance().addRoute(new InputRoute());//add inputroute to messagemanager
        app.getGraphicsTech().addAfterDrawStrategy(0,new UIDrawStrategy());
        
        IGraphicsTech gr=app.getGraphicsTech();
        UITextButton button = new UITextButton("text-button",new Vector(100,100,100),new StringProperty("Text Button"),gr.createFont("Dialog",
                IFont.FontStyle.BOLD, 20),gr.createColor(IColor.GeneraColor.ORANGE));

        button.setToCoverAction((ui)->{
            DebugUtil.log("Cover!!");
        });
        button.setToDownAction((ui)->{
            DebugUtil.log("Down!!");
        });
        button.setToNormalAction((ui)->{
            DebugUtil.log("Normal!!");
        });
        UIFace face=new UIFace("Face1");
        face.addUiInAll(button);
        InputManager.getInstance().addFaceInAll(face);
        InputManager.getInstance().activeFace("Face1");
        
        App.launch();
    }
}
