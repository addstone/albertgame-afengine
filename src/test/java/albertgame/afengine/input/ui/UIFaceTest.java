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
import albertgame.afengine.input.InputManager;
import albertgame.afengine.input.InputRoute;
import albertgame.afengine.input.UIActor;
import albertgame.afengine.input.UIFace;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.XmlUtil;
import org.dom4j.Element;

/**
 *
 * @author Admin
 */
public class UIFaceTest {
    public static class MenuStart implements IUIAction{
        int x;
        static int ix=0;
        public MenuStart() {
            this.x = ++ix;
        }

        @Override
        public void action(UIActor ui) {
            DebugUtil.log("Start Game! from ["+x+"]");
        }
    }
    public static class MenuContinue implements IUIAction{
        int x;
        static int ix=0;
        public MenuContinue() {
            this.x = ++ix;
        }
        @Override
        public void action(UIActor ui) {
            DebugUtil.log("Continue Game!from ["+x+"]");
        }
    }
    public static class MenuEnd implements IUIAction{
        int x;
        static int ix=0;
        public MenuEnd() {
            this.x = ++ix;
        }
        @Override
        public void action(UIActor ui) {
            DebugUtil.log("End Game!from ["+x+"]");
            App.exit();
        }
    }

    public static void main(String[] args) {
        DebugUtil.switchOn();//Open Debug
        FactoryUtil.putFactory("ui-menu","start",(params)->new MenuStart());//add factory
        FactoryUtil.putFactory("ui-menu","continue",(params)->new MenuContinue());
        FactoryUtil.putFactory("ui-menu","end",(params)->new MenuEnd());

        WindowApp app = new WindowApp(new IAppLogic.AppLogicBase(), "KeyInputTest", "", 800, 600);//instance a window app
        App.setInstance(app);

        MessageManager.getInstance().addRoute(new InputRoute());//add inputroute to messagemanager
        app.getGraphicsTech().addAfterDrawStrategy(0, new UIDrawStrategy());

        Element faceroot=XmlUtil.readXMLFileDocument(
                UIFaceTest.class.getClassLoader().getResource("afengine/asset/input/face1.xml"),
                false).getRootElement();
        UIFace face=UIControlHelp.loadFace(faceroot);

        InputManager.getInstance().addFaceInAll(face);
        InputManager.getInstance().activeFace(face.getFaceName());

        App.launch();
    }
}
