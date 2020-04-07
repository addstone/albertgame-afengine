/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.graphics.GraphicsTech_Java2DImpl;
import albertgame.afengine.graphics.IDrawStrategy;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;

public class GameCore implements IDrawStrategy {

    public static void main(String[] args) {
        new GameCore().run();
    }

    private final IGraphicsTech tech;
    private final ITexture img;

    public GameCore() {
        tech = new GraphicsTech_Java2DImpl();
        img = tech.createTexture(getClass().getClassLoader().getResource("img.JPG"));
    }

    public void run() {
        tech.setRootDrawStrategy(this);
        ITexture texture = tech.createTexture(getClass().getClassLoader().getResource("duke0.gif"));
        tech.create(800,600,texture, "Hello");
        long dt;
        long lt, nt;
        lt = System.currentTimeMillis();
        nt = lt;
        while (true) {
            lt = nt;
            nt = System.currentTimeMillis();
            dt = nt - lt;
            update(dt);
            tech.callDraw();
        }
    }

    //覆盖这个方法用以更新
    public void update(long delt) {

    }

    //覆盖这个方法，调用绘制
    @Override
    public void draw(IGraphicsTech tech) {
        tech.drawText(0, 0, tech.getFont(), tech.getColor(), "FPS:" + tech.getFPS());
        tech.drawTexture(0, 100, img);
    }
}
