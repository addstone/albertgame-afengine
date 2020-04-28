/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics;

import albertgame.afengine.util.DebugUtil;
import java.net.URL;


public class GraphicsTechTest implements IDrawStrategy {

    public static void main(String[] args) {
        new GraphicsTechTest().run();
    }

    private final IGraphicsTech tech;
    private IFont font1,font2;
    private IColor color1,color2;
    private ITexture texture1,texture2,texture3,texture4;

    public GraphicsTechTest() {
        tech = new GraphicsTech_Java2DImpl();
    }
    
    public void init(){
        DebugUtil.switchOn();
        
        ITexture texture = tech.createTexture(getClass().getClassLoader().getResource("duke0.gif"));
        
               
//测试drawstrategy
/**
    setRootDrawStrategy(IDrawStrategy strategy);
    addBeforeDrawStrategy(long priority,IDrawStrategy drawstrategy);
    addAfterDrawStrategy(long priority,IDrawStrategy drawstrategy);
 */        
        tech.setRootDrawStrategy(this);
        tech.addBeforeDrawStrategy(0,(tech)->{
            IColor oldc=tech.getColor();
            tech.setColor(color2);
            tech.drawText(100,100,tech.getFont(),tech.getColor(),"Draw---Before---0");
            tech.setColor(oldc);
        });
        tech.addBeforeDrawStrategy(1,(tech)->{
            IColor oldc=tech.getColor();
            tech.setColor(color1);
            tech.drawText(100,100,tech.getFont(),tech.getColor(),"draw-before-1");
            tech.setColor(oldc);
        });
        tech.addAfterDrawStrategy(0,(tech)->{
            IColor oldc=tech.getColor();
            tech.setColor(color2);
            tech.drawText(100,200,tech.getFont(),tech.getColor(),"Draw---After---0");
            tech.setColor(oldc);
        });
        tech.addAfterDrawStrategy(1,(tech)->{
            IColor oldc=tech.getColor();
            tech.setColor(color1);
            tech.drawText(100,200,tech.getFont(),tech.getColor(),"draw-after-1");
            tech.setColor(oldc);        
        });


//测试window方法
/**
    create(int x,int y,int w,int h,ITexture icon,String title);
    create(int w,int h,ITexture icon,String title);
    create(ITexture icon,String title);
    setTitle(String title);
    setIcon(ITexture texture);    
    moveWindowTo(int x,int y);
    getMoniterWidth();
    getMoniterHeight();
    getDisplayX();
    isFullWindow();
    getDisplayY();
    getWindowWidth();
    getWindowHeight();
    
    getTitle();
    getIcon();

    getMouseIcon();
    setMouseIcon(ITexture texture);
 */        
        tech.create(100,10,800,600,texture, "Hello");
        tech.setIcon(texture);
        tech.setMouseIcon(texture);
        tech.setTitle("GraphicsTechTest");
//        tech.moveWindowTo(10,10);
        int w=tech.getWindowWidth();
        int h=tech.getWindowHeight();
        int mw=tech.getMoniterWidth();
        int mh=tech.getMoniterHeight();
        DebugUtil.log("monitor:"+mw+"/"+mh);
        DebugUtil.log("window:"+w+"/"+h);
        
        IGraphicsCreate create=(IGraphicsCreate)tech;
        font1=create.createFont("宋体", IFont.FontStyle.BOLD,30);
//        tech.setFont(font1);
        font2=create.createFont(this.getClass().getClassLoader().getResource("text.ttf"), IFont.FontStyle.BOLD,20);
        color1=create.createColor(IColor.GeneraColor.BLUE);
        color2=create.createColor(100,200,30,100);
        texture1=create.createTexture("src/test/resources/img.JPG");
        texture2=create.createTexture("src/test/resources/img.JPG",250,300,50,50);
        URL url=this.getClass().getClassLoader().getResource("player.png");
        texture3=create.createTexture(url);
        texture4=create.createTexture(url,0,0,50,50);        
    }

    public void run(){
        init();

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
    public void draw(IGraphicsTech tech){
        tech.drawText(0, 0, tech.getFont(), tech.getColor(), "FPS:" + tech.getFPS());
        tech.drawText(100,400,font1, color1,"我的世界");
        //测试draw接口的方法
/**
    drawPoint(int x,int y);
    drawLine(int x1,int y1,int x2,int y2);
    drawPolygon(int[] x,int[] y,boolean fill);
    drawOval(int x,int y,int w,int h,boolean fill);    
    drawCircle(int x,int y,int radius,boolean fill);
    drawRoundRect(int x,int y,int width,int height,int arcWidth,int artHeight,boolean fill);
    drawTexture(int x,int y,ITexture texture);
    drawText(int x,int y,IFont font,IColor color,String text);
    drawTexts(int[] x,int[] y,IFont[] font,IColor[] color,String[] text); 
 */  
        tech.drawTexture(300,250, texture1);
        tech.drawTexture(300,300, texture2);
        tech.drawTexture(300,400, texture3);
        tech.drawTexture(300,500, texture4);

        tech.drawPoint(200,200);
        tech.drawLine(0,0,300,350);
        tech.drawPolygon(new int[]{100,200,200,100},new int[]{0,0,100,100}, true);
        tech.drawPolygon(new int[]{150,250,250,150},new int[]{0,0,100,100}, false);
        
        IColor oldc=tech.getColor();
        tech.setColor(color1);
        tech.drawOval(190,280,20,30,true);
        tech.drawOval(260,390,20,30,false);
        tech.drawCircle(320,320,40,true);
        tech.drawCircle(320,320,40,false);
        tech.setColor(color2);
        tech.drawRoundRect(170,290,120,50,10,10,true);
        tech.drawRoundRect(470,290,120,50,5,5,false);
        tech.setColor(oldc);
    }
}
