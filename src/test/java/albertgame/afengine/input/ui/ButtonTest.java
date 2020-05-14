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
import albertgame.afengine.graphics.ITexture;
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

    private static boolean show = true;

    public static void main(String[] args) {
        DebugUtil.switchOn();//Open Debug

        WindowApp app = new WindowApp(new IAppLogic.AppLogicBase(), "KeyInputTest", "", 800, 600);//instance a window app
        App.setInstance(app);

        MessageManager.getInstance().addRoute(new InputRoute());//add inputroute to messagemanager
        app.getGraphicsTech().addAfterDrawStrategy(0, new UIDrawStrategy());

        IGraphicsTech gr = app.getGraphicsTech();

        final IColor redc = gr.createColor(IColor.GeneraColor.RED);
        final IColor bluec = gr.createColor(IColor.GeneraColor.BLUE);
        final IColor orangec = gr.createColor(IColor.GeneraColor.ORANGE);

        UITextButton button = new UITextButton("text-button", 100, 100, "Text Button");
        UITextButton exit = new UITextButton("text-button", 10, 10, "OOO");
        exit.setToDownAction((ui) -> {
            App.exit();
        });

        button.setToCoverAction((ui) -> {
            DebugUtil.log("Cover!!");
            button.setFontColor(redc);
        });

        button.setToDownAction((ui) -> {
            DebugUtil.log("Down!!");
            button.setFontColor(bluec);
            if (show) {
                InputManager.getInstance().activeFace("Face2");
            } else {
                InputManager.getInstance().hideFace("Face2");
            }
            show = !show;
        });
        button.setToNormalAction((ui) -> {
            DebugUtil.log("Normal!!");
            button.setFontColor(orangec);
        });

        UIInputLine line = new UIInputLine("inputline", 400, 200, 20,
                redc, gr.createFont("Dialog", IFont.FontStyle.BOLD, 20));
        ITexture img = gr.createTexture(ButtonTest.class.getClassLoader().getResource("img.JPG"), 200, 200, 120, 40);
        line.setBack(img);
        line.enableSecretMode(true);

        ITexture texture1 = gr.createTexture(ButtonTest.class.getClassLoader().getResource("duke0.gif"));
        ITexture texture2 = gr.createTexture(ButtonTest.class.getClassLoader().getResource("duke1.gif"));
        ITexture texture3 = gr.createTexture(ButtonTest.class.getClassLoader().getResource("duke2.gif"));
        UIImageButton button2 = new UIImageButton("imagebutton", 100, 200, texture1, texture2, texture3);
        button2.setToCoverAction((ui) -> {
            DebugUtil.log("Image Button Cover!");
            line.enableSecretMode(false);
        });
        button2.setToDownAction((ui) -> {
            DebugUtil.log("Image Button Down!");
        });
        button2.setToNormalAction((ui) -> {
            DebugUtil.log("Image Button Normal!");
            line.enableSecretMode(true);
        });

        UIToggle toggle = new UIToggle("toggle", 300, 300, 2);
        toggle.pushToggle(texture1, (ui) -> {
            DebugUtil.log("Toggle 1!!");
        });
        toggle.pushToggle(texture2, (ui) -> {
            DebugUtil.log("Toggle 2!!");
        });
        toggle.pushToggle(texture3, (ui) -> {
            DebugUtil.log("Toggle 3!!");
        });

        UITextLabel label = new UITextLabel("label1", 100, 300, "Label !!!");
        UIImageLabel label2 = new UIImageLabel("label2", 200, 400, texture2);

        UIPane pane = new UIPane("pane", 200, 200);
        pane.addChild(toggle);
        pane.addChild(line);

        UIButtonList blist = new UIButtonList("blist", 20, 20);
        blist.addUiButton(button);
        blist.addUiButton(button2);

        UIFace face = new UIFace("Face1");


//        if(face.containsUiInReserved(blist.getUiName())){
//            DebugUtil.log("add ButtonList And Reserve Successfully");
//        }
//        face.removeUiInReserved(blist.getUiName());
//        if(!face.containsUiInReserved(blist.getUiName())){
//            DebugUtil.log("remove ButtonList And Reserve Successfully");
//        }
//        if(face.containsUiInAll(blist.getUiName())){
//            DebugUtil.log("still have ButtonList In Face ,Successfully");
//        }
//        face.addUiInAllAndReserve(blist);
//        face.removeUiInAll(blist.getUiName());
//        if(!face.containsUiInAll(blist.getUiName())){
//            DebugUtil.log("Femove ButtonList In Face ,Successfully");
//        }
        
               
        face.addUiInAll(exit,label, label2);

        UIFace face2 = new UIFace("Face2");
        face2.addUiInAll(pane);

        InputManager.getInstance().addFaceInAll(face);
        InputManager.getInstance().addFaceInAll(face2);

        InputManager.getInstance().activeFace("Face1");

        App.launch();
    }
}
