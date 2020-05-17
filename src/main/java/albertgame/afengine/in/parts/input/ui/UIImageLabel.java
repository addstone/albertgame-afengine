package albertgame.afengine.in.parts.input.ui;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.graphics.ITexture;
import albertgame.afengine.in.parts.input.UIActor;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.math.Vector;
import albertgame.afengine.core.util.property.ValueProperty;
import org.dom4j.Element;

public class UIImageLabel extends UIActor {
    
    private ValueProperty<ITexture> textureValue;
    private boolean dirty=false;
    
    public UIImageLabel(String name,int x,int y,ITexture texture){
        this(name,new Vector(x,y,0));
        textureValue=new ValueProperty<>(texture);
    }
    
    public UIImageLabel(String name, Vector pos) {
        super(name, pos);
    }

    public ITexture getTexture() {
        return textureValue.get();
    }

    public void setTexture(ITexture texture) {
        this.textureValue.set(texture);
        dirty=true;
    }

    public ValueProperty<ITexture> getTextureValue() {
        return textureValue;
    }

    public void setTextureValue(ValueProperty<ITexture> textureValue) {
        this.textureValue = textureValue;
    }
    
    @Override
    public void draw(IGraphicsTech tech){
        if(dirty){
            if(this.textureValue!=null){
                super.width=textureValue.get().getWidth();
                super.height=textureValue.get().getHeight();
            }
            dirty=false;
        }

        if(textureValue!=null){
            int dx=super.getUiX();
            int dy=super.getUiY();
            tech.drawTexture(dx, dy, textureValue.get());
        }        
    }    
    
    public static class UIImageLabelCreator implements IUICreator{
        /*
            <UIImageLabel name pos>
                <texture path=""/>
            </UIImageLabel>
        */
        @Override
        public UIActor createUi(Element element){
            String name=element.attributeValue("name");
            Vector pos =createPos(element);
            if(pos==null)
                pos=new Vector(10,10,0,0);
            if(name==null)
                name="DefaultUiName"+IDCreator.createId();
            Element texture=element.element("texture");            
            ITexture normalimage=null;
            if(texture==null){
                DebugUtil.log("no texture for label defined.!");
            }else{
                normalimage=createTexture(texture);                
            }
            
            if(normalimage==null){
                DebugUtil.log("normal texture for label creation failed.!");                
                return null;
            }
            
            UIImageLabel button=new UIImageLabel(name,pos);
            
            return button;
        }        
        private final IGraphicsTech tech=((WindowApp)App.getInstance()).getGraphicsTech();
        private Vector createPos(Element element){
            String poss=element.attributeValue("pos");
            String[] posl=poss.split(",");
            double x = Double.parseDouble(posl[0]);
            double y = Double.parseDouble(posl[1]);
            return new Vector(x,y,0,0);
        }
        private ITexture createTexture(Element element){
            String path=element.attributeValue("path");
            if(path==null){
                DebugUtil.log("path for texture is not defined.return null texture");
                return null;
            }
            else return tech.createTexture(path);
        }        
    }
}
