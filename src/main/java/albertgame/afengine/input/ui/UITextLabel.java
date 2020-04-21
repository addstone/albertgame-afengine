package albertgame.afengine.input.ui;

import albertgame.afengine.app.App;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.graphics.IColor;
import albertgame.afengine.graphics.IFont;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.input.UIActor;
import albertgame.afengine.util.TextUtil;
import albertgame.afengine.util.math.Vector;
import albertgame.afengine.util.property.StringProperty;
import org.dom4j.Element;

/**
 *
 * @author Admin
 */
public class UITextLabel extends UIActor{
    
    private StringProperty text;
    private IColor color;
    private IFont font;
    private boolean hdirty=false;
    public UITextLabel(String name,Vector pos,StringProperty text){
        this(name,pos,text,null,null);        
    }
    public UITextLabel(String name, Vector pos,StringProperty text,IFont font,IColor color) {
        super(name, pos);
        this.text=text;
        this.color=color;
        this.font=font;
    }

    public StringProperty getText() {
        return text;
    }

    public void setText(StringProperty text) {
        this.text = text;        
    }

    public IColor getColor() {
        return color;
    }

    
    public void setColor(IColor color) {
        this.color = color;
    }

    public IFont getFont() {
        return font;
    }

    public void setFont(IFont font) {
        this.font = font;
        hdirty=true;
    }

    @Override
    public void update(long time){
        if(hdirty){
            if(font!=null&&text!=null&&text.get()!=null){
                super.height=font.getFontHeight();
            }            
            hdirty=false;
        }
        super.width=font.getFontWidth(text.get());
    }

    
    @Override
    public void draw(IGraphicsTech tech) {
        if(text!=null&&text.get()!=null){
            if(font==null){
                font=tech.getFont();
            }
            if(color==null){
                color=tech.getColor();
            }
            int dx=super.getUiX();
            int dy=super.getUiY();
            tech.drawText(dx, dy, font, color, text.get());
        }        
    }    
    
    
    public static class UITextLabelCreator implements IUICreator{
        /*
            <UITextLabel name pos>
        *      <text></text>
        *      <font path=""></font>
        *      <size></size>
        *      <color></color>                
            </UITextLabel>
        */
        @Override
        public UIActor createUi(Element element){
            String name=element.attributeValue("name");
            Vector pos =createPos(element);
            if(pos==null)
                pos=new Vector(10,10,0,0);
            if(name==null)
                name="DefaultUiName"+IDCreator.createId();
            
            StringProperty text;
            IFont font;
            IColor color=null;
            IColor backcolor=null;
            Element texte = element.element("text");
            if(texte!=null){
                text=new StringProperty(TextUtil.getRealValue(texte.getText(),null));
            }
            else{
                text=new StringProperty("DefaultText");
            }
            Element fonte = element.element("font");
            String sizes = element.elementText("size");
            String path=null;
            if(fonte==null){
                font= ((IGraphicsTech)((WindowApp)App.getInstance())
                        .getGraphicsTech()).createFont("Dialog",
                                IFont.FontStyle.PLAIN, 30);
            }
            else if(fonte.attribute("path")!=null){
                path=fonte.attributeValue("path");
                    font = ((IGraphicsTech)((WindowApp)App.getInstance()).
                        getGraphicsTech()).createFontByPath(fonte.getText(),IFont.FontStyle.PLAIN, Integer.parseInt(sizes));                        
            }
            else{
                font= ((IGraphicsTech)((WindowApp)App.getInstance())
                        .getGraphicsTech()).createFont("Dialog",
                                IFont.FontStyle.PLAIN, 30);            
            }
            Element colore = element.element("color");
            String colors;

            if(colore==null){
               colors=IColor.GeneraColor.ORANGE.toString();
            }
            else colors = element.elementText("color");

            color=((IGraphicsTech)((WindowApp)App.getInstance()).
                    getGraphicsTech()).createColor(IColor.GeneraColor.valueOf(colors));
                       
            UITextLabel label=new UITextLabel(name,pos,text,font,color);
            return label;            
        }                
         private Vector createPos(Element element){
            String poss=element.attributeValue("pos");
            String[] posl=poss.split(",");
            double x = Double.parseDouble(posl[0]);
            double y = Double.parseDouble(posl[1]);
            return new Vector(x,y,0,0);
        }
    }
}
