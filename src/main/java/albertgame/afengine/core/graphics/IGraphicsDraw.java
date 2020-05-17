/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.graphics;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;

/**
 * The Draw Methods for Graphics,Provide Basic Draw Function,
 * Such as Draw Point,Line,Polygon,Oval,Rect,Texture and so on.
 * @author Albert Flex
 */
public interface IGraphicsDraw {
    public static IGraphicsDraw draw(){
        return ((WindowApp) App.getInstance()).getGraphicsTech();
    }
    /**
     * When GraphicsTech is Drawing(called callDraw),this methods will return true,
     * and when callDraw method is done,this method will return false
     * @return
     */
    public boolean isDrawing();

    /**
     * You should call this method,if you want to use the drawstrategy,and when you call this method
     * then graphics draw will call inner drawstrategies you setted.
     */
    public void callDraw();//绘制图形
    
    public void setRootDrawStrategy(IDrawStrategy strategy);
    public void addBeforeDrawStrategy(long priority,IDrawStrategy drawstrategy);
    public void addAfterDrawStrategy(long priority,IDrawStrategy drawstrategy);
    
    public void clear(int x,int y,int width,int height,IColor color);
    public void drawPoint(int x,int y);
    public void drawLine(int x1,int y1,int x2,int y2);
    public void drawPolygon(int[] x,int[] y,boolean fill);
    public void drawOval(int x,int y,int w,int h,boolean fill);    
    public void drawCircle(int x,int y,int radius,boolean fill);
    public void drawRoundRect(int x,int y,int width,int height,int arcWidth,int artHeight,boolean fill);
    
    public void drawTexture(int x,int y,ITexture texture);
    public void drawText(int x,int y,IFont font,IColor color,String text);
    public void drawTexts(int[] x,int[] y,IFont[] font,IColor[] color,String[] text);
    
    //use this draw for extensions.
    public void drawOther(String drawType,Object[] value);     
}
