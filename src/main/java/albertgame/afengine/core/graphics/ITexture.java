/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.graphics;

/**
 *
 * @author Albert Flex
 */
public interface ITexture {
    
    public int getWidth();
    public int getHeight();

    public ITexture scaleInstance(double sx, double sy);
    public ITexture cutInstance(int x, int y, int w, int h);
}
