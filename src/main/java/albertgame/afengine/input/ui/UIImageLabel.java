package albertgame.afengine.input.ui;

import albertgame.afengine.app.App;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import albertgame.afengine.input.UIActor;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.math.Vector;
import org.dom4j.Element;

public class UIImageLabel extends UIActor{
    
    private ITexture texture;
    private boolean dirty=false;
    public UIImageLabel(String name, Vector pos) {
        super(name, pos);
    }

    public ITexture getTexture() {
        return texture;
    }

    public void setTexture(ITexture texture) {
        this.texture = texture;
        dirty=true;
    }

    @Override
    public void update(long time) {
        if(dirty){
            if(this.texture!=null){
                super.width=texture.getWidth();
                super.height=texture.getHeight();
            }
            dirty=false;
        }
    }
    
    @Override
    public void draw(IGraphicsTech tech){
        if(texture!=null){
            int dx=super.getUiX();
            int dy=super.getUiY();
            tech.drawTexture(dx, dy, texture);
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
