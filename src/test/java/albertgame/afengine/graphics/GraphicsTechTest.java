/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics;


public class GraphicsTechTest implements IDrawStrategy {

    public static void main(String[] args) {
        new GraphicsTechTest().run();
    }

    private final IGraphicsTech tech;
    private IFont font1,font2;

    public GraphicsTechTest() {
        tech = new GraphicsTech_Java2DImpl();
    }
    
    public void init(){
        ITexture texture = tech.createTexture(getClass().getClassLoader().getResource("duke0.gif"));
        tech.create(800,600,texture, "Hello");
        tech.setMouseIcon(texture);
        
        IGraphicsCreate create=(IGraphicsCreate)tech;
        font1=create.createFont("宋体", IFont.FontStyle.BOLD,30);
        tech.setFont(font1);

//测试drawstrategy
/**
    setRootDrawStrategy(IDrawStrategy strategy);
    addBeforeDrawStrategy(long priority,IDrawStrategy drawstrategy);
    addAfterDrawStrategy(long priority,IDrawStrategy drawstrategy);
 */        


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

//测试create方法
/**
    createTexture(String iconPath);
    createTexture(String iconPath, int cutFromX, int cutFromY, int cutWidth, int cutHeight);
    createTexture(URL url);
    createTexture(URL url, int cutFromX, int cutFromY, int cutWidth, int cutHeight);
    createFont(String fontName, IFont.FontStyle style, int size);
    createFontByPath(String fontPath, IFont.FontStyle style, int size);
    createFont(URL url, IFont.FontStyle style, int size);
    createColor(int red, int green, int blue, int alpha);
    createColor(IColor.GeneraColor colortype);
         */
        
//测试state方法
/**
    getRenderName();
    getFont();
    setFont(IFont font);
    getColor();
    setColor(IColor color);
    getFPS();
    getValue(String name);
    setValue(String name, Object obj[]);
 */        
        
//测试adjust方法
/**
    adjust(int x,int y,int w,int h);
    adjust(int w,int h);
    adjustFull();
    addAdjustHandler(IWindowAdjustHandler handler);    
*/        
    }

    public void run(){
        tech.setRootDrawStrategy(this);
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
    }
}
