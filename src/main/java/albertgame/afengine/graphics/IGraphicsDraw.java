/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics;

/**
 *
 * @author Albert Flex
 */
public interface IGraphicsDraw {

    public boolean isDrawing();
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
