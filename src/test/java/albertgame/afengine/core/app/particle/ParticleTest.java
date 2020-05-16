/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.app.particle;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.IAppLogic;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IColor;
import albertgame.afengine.core.graphics.IDrawStrategy;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.math.Vector;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ParticleTest implements IDrawStrategy, IAppLogic {

    public static void main(String[] args) {
        new ParticleTest().launchWindowTest();
    }

    private ParticleCloud cloud;

    public void launchWindowTest() {

        DebugUtil.switchOn();
        WindowApp win = new WindowApp(this, "ParticleTest", "src/test/resources/duke0.gif", 800, 600);
        IGraphicsTech tech = win.getGraphicsTech();
        tech.setRootDrawStrategy(this);

        App.launch(win);
    }

    @Override
    public void draw(IGraphicsTech tech) {
        List<Particle> plist = cloud.getPlist();
        try {
            for (int i = 0; i != plist.size(); ++i) {
                Particle p = plist.get(i);
                int x = (int) p.position.getX();
                int y = (int) p.position.getY();
                IColor col = p.color;
                int w = p.width;
                int h = p.height;
                IColor oldc = tech.getColor();
                tech.setColor(col);
                tech.drawOval(x, y, w, h, true);
                tech.setColor(oldc);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean init() {
        Vector pos = new Vector(300, 300, 0, 0);
        Vector vel = new Vector(0, 0, 0, 0);
        Vector acc = new Vector(0, 0, 0);
        IGraphicsTech tech = ((WindowApp) (App.getInstance())).getGraphicsTech();
        cloud = new ParticleCloud(pos, vel, acc, tech.createColor(IColor.GeneraColor.ORANGE),
                true, 5000,5,5);
        cloud.emit(1000);
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
}
