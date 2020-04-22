/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.graphics.GraphicsTech_Java2DImpl;
import albertgame.afengine.graphics.IColor;
import albertgame.afengine.graphics.IDrawStrategy;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import java.util.Random;

public class GameCore implements IDrawStrategy {

    public static void main(String[] args) {
        new GameCore().run();
    }

    private final IGraphicsTech tech;
    private final ITexture img[];
    private final ITexture img1;

    public GameCore() {
        tech = new GraphicsTech_Java2DImpl();
        img1 = tech.createTexture(getClass().getClassLoader().getResource("tou.png"), 0, 0, 100, 100);
        img = new ITexture[1000];
        for (int i = 0; i != 1000; ++i) {
            img[i] = img1.scaleInstance(0.5, 0.5);
        }
    }

    public void run(){
        tech.setRootDrawStrategy(this);
        ITexture texture = tech.createTexture(getClass().getClassLoader().getResource("duke0.gif"));
        tech.create(800,600,texture, "Hello");
        tech.setMouseIcon(texture);
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
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException ex) {
//        }
    }

    //覆盖这个方法，调用绘制
    @Override
    public void draw(IGraphicsTech tech){
        tech.drawText(0, 0, tech.getFont(), tech.getColor(), "FPS:" + tech.getFPS());

        IColor oldc = tech.getColor();
        tech.setColor(tech.createColor(100, 100, 100, 100));
        tech.drawRoundRect(0, 100, 400, 400, 0, 0, true);
        tech.setColor(oldc);
        Random ran=new Random();
        for (ITexture im : img){
            tech.drawTexture(ran.nextInt(tech.getWindowWidth()),ran.nextInt(tech.getWindowHeight()), im);
        }
        tech.drawTexture(200, 0, img1);
    }
}
