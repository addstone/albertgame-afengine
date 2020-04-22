/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.graphics.GraphicsTech_Java2DImpl;
import albertgame.afengine.graphics.IDrawStrategy;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.FactoryUtil.IFactory;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class AppBootTest1 implements IAppLogic, IDrawStrategy {

    public static void main(String[] args) {
        URL url = AppBootTest1.class.getClassLoader().getResource("afengine/asset/app/boot1.xml");
        System.out.println(url.toString());
        AppBoot.boot(url);
    }

    static {
        IFactory fac = (args) -> {
            return new AppBootTest1();
        };
        FactoryUtil.putFactory("boot", "boot1", fac);

        IFactory fac2 = (args) -> {
            return new GraphicsTech_Java2DImpl();
        };
        FactoryUtil.putFactory("graphics","java2dimpl", fac2);
    }
    public boolean init() {
        return true;
    }

    @Override
    public boolean update(long time) {
        return true;
    }

    @Override
    public boolean shutdown() {
        return true;
    }

    @Override
    public void draw(IGraphicsTech tech) {
        tech.drawText(0,20, tech.getFont(), tech.getColor(),"W/H:"+tech.getWindowWidth()+"/"+tech.getWindowHeight());
        tech.drawText(0,tech.getWindowHeight()-tech.getFont().getFontHeight(),tech.getFont(),tech.getColor(),"FPS:"+tech.getFPS());
    }
}
