package albertgame.afengine.scene.component.render;

import albertgame.afengine.graphics.IColor;
import albertgame.afengine.graphics.IFont;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.scene.SceneCamera;
import albertgame.afengine.util.math.Vector;
import albertgame.afengine.util.property.StringProperty;

/**
 * text render<br>
 * @see RenderComponent
 * @author Albert Flex
 */
public class TextRenderComponent extends RenderComponent{

    private IFont font;
    private IColor color;
    private StringProperty text;
    private IColor backColor;
    private boolean hasBack;

    public TextRenderComponent(IFont font,IColor color,StringProperty text) {
        super();
        this.font=font;
        super.renderWidth=font.getFontWidth(text.get());
        super.renderHeight=font.getFontHeight();
        this.color=color;
        this.text=text;
        hasBack=false;
        backColor=null;
    }        
    public TextRenderComponent(IFont font,IColor color,IColor back,StringProperty text){
        this(font,color,text);
        hasBack=true;
        backColor=back;
    }

    public IColor getBackColor() {
        return backColor;
    }

    public boolean isHasBack() {
        return hasBack;
    }

    public void setBackColor(IColor backColor) {
        if(!hasBack)hasBack=true;
        
        this.backColor = backColor;
    }

    public IColor getColor() {
        return color;
    }

    public void setColor(IColor color) {
        this.color = color;
    }

    
    @Override
    public void render(SceneCamera camera,IGraphicsTech tech){
        double ax=super.getRenderX(camera);
        double ay=super.getRenderY(camera);
        Vector a=this.getActor().getTransform().anchor;
        float width = font.getFontWidth(text.get());
        float height=(float) (font.getFontHeight());
        float dx = (float) (ax-width*a.getX());
        float dy = (float) (ay-(height+height*a.getY()));
        
        int x1 = (int)dx;
        int x2 = (int)(dx+width);
        int y1 = (int)(dy);
        int y2 = (int)(dy+height);
        int[] x = new int[]{x1,x2,x2,x1};
        int[] y = new int[]{y1,y1,y2,y2};
        if(hasBack){
            IColor oldc = tech.getColor();
           tech.setColor(backColor);
           tech.drawPolygon(x,y,true);
           tech.setColor(oldc);
        }
       tech.drawText((int)dx,(int)(dy+height*0.9),font,color,text.get());
        super.renderWidth=font.getFontWidth(text.get());
        super.renderHeight=font.getFontHeight();       
    }

    public StringProperty getText() {
        return text;
    }
}
