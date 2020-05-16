/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.app;

import albertgame.afengine.core.message.XMLMessagePartBoot;
import albertgame.afengine.in.core.graphics.GraphicsTech_Java2DImpl;
import albertgame.afengine.core.graphics.IDrawStrategy;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.input.InputManager;
import albertgame.afengine.core.input.InputRoute;
import albertgame.afengine.core.input.InputServlet;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.FactoryUtil;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class AppBootTest1 implements IAppLogic{

    public static class Draw implements IDrawStrategy {
        @Override
        public void draw(IGraphicsTech tech) {
            tech.drawText(0, 20, tech.getFont(), tech.getColor(), "W/H:" + tech.getWindowWidth() + "/" + tech.getWindowHeight());
        }
    }

    public static void main(String[] args) {
        URL url = AppBootTest1.class.getClassLoader().getResource("afengine/asset/app/boot1.xml");
        System.out.println(url.toString());
        AppBoot.boot(url);
    }

    static {
        FactoryUtil.putFactory("boot", "boot1",(args)->new AppBootTest1());
        FactoryUtil.putFactory("graphics", "java2dimpl",(args)->new GraphicsTech_Java2DImpl());
        FactoryUtil.putFactory("msg-route", "ui", (args)->new InputRoute());
        FactoryUtil.putFactory("type-boot", "win", (args)->new WindowApp.WindowAppBoot());
        FactoryUtil.putFactory("drawstrategy", "debug", (args)->new DebugUtil.DebugDrawStrategy());
        FactoryUtil.putFactory("xmlpartboot", "message", (args)->new XMLMessagePartBoot());
        FactoryUtil.putFactory("drawstrategy", "root", (args)->new Draw());
    }

    @Override
    public boolean init() {
        DebugUtil.log("init logic");
        InputManager.getInstance().addPreServlet(InputServlet.EventCode_KeyUp, InputServlet.ExitServlet);
        return true;
    }

    @Override
    public boolean update(long time) {
        return true;
    }

    @Override
    public boolean shutdown() {
        DebugUtil.log("shutdown logic");
        return true;
    }
}
