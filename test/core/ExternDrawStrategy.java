package core;

import afengine.component.render.GraphicsTech_Java2D;
import afengine.core.util.XMLEngineBoot;
import afengine.core.window.IDrawStrategy;
import afengine.core.window.IGraphicsTech;
import java.awt.Color;
import java.awt.Graphics2D;

public class ExternDrawStrategy implements IDrawStrategy{

    public static void main(String[] args) {
        XMLEngineBoot.bootEngine("test/assets/exdraw.xml");
//      System.out.println(WindowApp.WindowAppBoot.class.getName());
    }
    
    double i=0;
    
    @Override
    public void draw(IGraphicsTech tech) {
        i+=0.1;
        if(i>255.0)
            i=0;
        Graphics2D graphics=((GraphicsTech_Java2D)tech).getGraphics();
        Color oldc=graphics.getColor();
        graphics.setColor(new Color(255,255,166, (int) i));
        graphics.fillRoundRect(100, 100, 300, 80, 20, 20);
        graphics.setColor(oldc);
    }    
}
