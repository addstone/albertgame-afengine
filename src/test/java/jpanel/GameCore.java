/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpanel;

import java.awt.Graphics2D;


/**
 *
 * @author Administrator
 */
public class GameCore{

    public static void main(String[] args) {
        new GameCore().run();
    }

    private Growing growing;
    
    public GameCore() {
        growing=new Growing();
//        growing.setDraw(this);
    }
    
    public void run() {
        growing.create(800,600,"Hello");
        long dt;
        long lt, nt;
        lt = System.currentTimeMillis();
        nt = lt;
        while (true) {
            lt = nt;
            nt = System.currentTimeMillis();
            dt = nt - lt;
            update(dt);
            growing.repaint();
        }
    }

    //覆盖这个方法用以更新
    public void update(long delt) {

    }

//    //覆盖这个方法，调用绘制
//    @Override
//    public void draw(Graphics2D graphics) {
//        System.out.println("draw");
//        graphics.drawString("FPS:"+growing.fps, 0, 60);
//    }    
}
