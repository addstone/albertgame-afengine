/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics;

/**
 *
 * @author Administrator
 */
public interface IGraphicsWindow {
    public void create(int x,int y,int w,int h,ITexture icon,String title);
    public void create(int w,int h,ITexture icon,String title);
    public void create(ITexture icon,String title);
    
    public void setTitle(String title);
    public void setIcon(ITexture texture);    
    
    public void adjust(int x,int y,int w,int h);
    public void adjust(int w,int h);
    public void adjustFull();
    public void addAdjustHandler(IWindowAdjustHandler handler);    
    
    public void destroy();   

    public void moveWindowTo(int x,int y);

    public int getMoniterWidth();
    public int getMoniterHeight();
    public int getDisplayX();
    public boolean isFullWindow();
    public int getDisplayY();
    public int getWindowWidth();
    public int getWindowHeight();
    
    public String getTitle();
    public ITexture getIcon();

    public ITexture getMouseIcon();
    public void     setMouseIcon(ITexture texture);
}
