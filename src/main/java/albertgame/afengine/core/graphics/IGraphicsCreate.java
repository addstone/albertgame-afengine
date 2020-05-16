/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.graphics;

import java.net.URL;

/**
 *
 * @author Albert Flex
 */
public interface IGraphicsCreate {

    public ITexture createTexture(String iconPath);
    public ITexture createTexture(String iconPath, int cutFromX, int cutFromY, int cutWidth, int cutHeight);
    public ITexture createTexture(URL url);
    public ITexture createTexture(URL url, int cutFromX, int cutFromY, int cutWidth, int cutHeight);

    public IFont createFont(String fontName, IFont.FontStyle style, int size);
    public IFont createFontByPath(String fontPath, IFont.FontStyle style, int size);
    public IFont createFont(URL url, IFont.FontStyle style, int size);

    public IColor createColor(int red, int green, int blue, int alpha);
    public IColor createColor(IColor.GeneraColor colortype);
}
