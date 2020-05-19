package albertgame.afengine.in.parts.input.ui;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.in.parts.input.UIActor;
import albertgame.afengine.core.message.Message;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.graphics.ITexture;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.math.IDGenerator;
import albertgame.afengine.core.util.math.Vector;
import org.dom4j.Element;

public class UIPane extends UIActor {
    private ITexture back;    
    
    public UIPane(String name,int x,int y){
        this(name,new Vector(x,y,0));
    }
    public UIPane(String name, Vector pos) {
        super(name, pos);
    }        
    
    @Override
    public boolean handle(Message msg){
        for(UIActor ui:super.children){
            if(ui.handle(msg))return true;
        }
        return false;
    }

    @Override
    public void draw(IGraphicsTech tech){
        if(back!=null){
            super.width=back.getWidth();
            super.height=back.getHeight();
        }        

        if(back!=null){
            int dx=super.getUiX();
            int dy=super.getUiY();
            tech.drawTexture(dx, dy, back);
        }
        super.children.forEach((ui) -> {
            ui.draw(tech);
        });
    }    

    public ITexture getBack() {
        return back;
    }

    public void setBack(ITexture back) {
        this.back = back;
    }    
    
    @Override
    public void addChild(UIActor ui){
        super.addChild(ui);
    }
    
    public static class UIPaneCreator implements IUICreator{

        /*
            <UIPane name pos back="">
                <UIPane name pos>
                    <UIActor/>
                </UIPane>
                <UIActor/>
                <UIActor/>
                <UIActor/>
                <UIActor/>
            </UIPane>
        */
        @Override
        public UIActor createUi(Element element){
           String name=element.attributeValue("name");
            Vector pos =createPos(element);
            if(pos==null)
                pos=new Vector(10,10,0,0);
            if(name==null)
                name="DefaultUiName"+IDGenerator.createId();
            String sback=element.attributeValue("back");
            ITexture back=null;
            if(sback!=null)
                back=createTexture(sback);
            UIPane pane=new UIPane(name,pos);            
            pane.setBack(back);
            return pane;
        }        
        private final IGraphicsTech tech=((WindowApp)App.getInstance()).getGraphicsTech();
        private Vector createPos(Element element){
            String poss=element.attributeValue("pos");
            String[] posl=poss.split(",");
            double x = Double.parseDouble(posl[0]);
            double y = Double.parseDouble(posl[1]);
            return new Vector(x,y,0,0);
        }
        private ITexture createTexture(String path){
            if(path==null){
                DebugUtil.log("path for texture is not defined.return null texture");
                return null;
            }
            else return tech.createTexture(path);
        }        
    }
}
