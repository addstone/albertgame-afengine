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
public interface IGraphicsState {

    public String getRenderName();
    public IFont getFont();
    public void setFont(IFont font);
    public IColor getColor();
    public void setColor(IColor color);
    public int getFPS();

    public Object[] getValue(String name);
    public void setValue(String name, Object obj[]);
}
