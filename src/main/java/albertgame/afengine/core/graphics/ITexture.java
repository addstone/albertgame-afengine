/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.graphics;

/**
 * The Plaint Texture Interface of GraphicsTech,
 * you could use this interface to manipulate the image or something.
 * you can create on GraphicsCreate interface which supported by GraphicsTech
 * @See IGraphicsCreate
 * @author Albert Flex
 */
public interface ITexture {
    
    public int getWidth();
    public int getHeight();

    public ITexture scaleInstance(double sx, double sy);
    public ITexture cutInstance(int x, int y, int w, int h);
}
