package albertgame.afengine.in.core.input.ui;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.graphics.ITexture;
import albertgame.afengine.core.input.UIActor;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.FactoryUtil;
import albertgame.afengine.core.util.math.Vector;
import albertgame.afengine.core.util.property.ValueProperty;
import org.dom4j.Element;

import java.net.MalformedURLException;
import java.net.URL;

public class UIImageButton extends UIButtonBase{
    
    private ValueProperty<ITexture> normalTextureValue,downTextureValue,coverTextureValue;
    private ITexture normalTexture,downTexture,coverTexture;
    private ITexture now;
    
    public UIImageButton(String name,int x,int y,ITexture normal,ITexture down,ITexture cover){
        this(name,new Vector(x,y,0),normal,down,cover);
    }
    public UIImageButton(String name,Vector pos,ITexture normal,ITexture down,ITexture cover) {
        super(name, pos);
        this.normalTexture=normal;
        this.downTexture=down;
        this.coverTexture=cover;
        this.normalTextureValue=new ValueProperty<>(normal);
        this.coverTextureValue=new ValueProperty<>(cover);
        this.downTextureValue=new ValueProperty<>(down);
        this.now=normalTexture;
        if(this.now!=null){
            super.width=this.now.getWidth();
            super.height=this.now.getHeight();
        }
    }

    public ITexture getNormalTexture(){        
        return normalTextureValue.get();
    }

    public void setNormalTexture(ITexture normalTexture) {
        normalTextureValue.set(normalTexture);
        this.normalTexture = normalTexture;        
    }

    public ITexture getDownTexture() {
        return downTextureValue.get();
    }

    public void setDownTexture(ITexture downTexture) {
        downTextureValue.set(downTexture);
        this.downTexture=downTexture;
    }

    public ITexture getCoverTexture() {
        return coverTextureValue.get();        
    }

    public void setCoverTexture(ITexture coverTexture) {
        coverTextureValue.set(coverTexture);
        this.coverTexture = coverTexture;
    }

    public ValueProperty<ITexture> getNormalTextureValue() {
        return normalTextureValue;
    }

    public void setNormalTextureValue(ValueProperty<ITexture> normalTextureValue) {
        this.normalTextureValue = normalTextureValue;
    }

    public ValueProperty<ITexture> getDownTextureValue() {
        return downTextureValue;
    }

    public void setDownTextureValue(ValueProperty<ITexture> downTextureValue) {
        this.downTextureValue = downTextureValue;
    }

    public ValueProperty<ITexture> getCoverTextureValue() {
        return coverTextureValue;
    }

    public void setCoverTextureValue(ValueProperty<ITexture> coverTextureValue) {
        this.coverTextureValue = coverTextureValue;
    }

    
    @Override
    protected void doDown() {
        if(downTexture!=null){
            this.now=this.downTexture;
            super.width=now.getWidth();
            super.height=now.getHeight();            
        }
    }

    @Override
    protected void doNormal() {
        if(normalTexture!=null){
            this.now=this.normalTexture;
            super.width=now.getWidth();
            super.height=now.getHeight();            
        }
    }

    @Override
    protected void doCover() {
        if(coverTexture!=null){
            this.now=this.coverTexture;
            super.width=now.getWidth();
            super.height=now.getHeight();            
        }
    }

    @Override
    public void draw(IGraphicsTech tech) {
        if(now!=null){
            int dx = this.getUiX();
            int dy = this.getUiY();
            tech.drawTexture(dx, dy, now);
        }        
    }           
    
    public static class UIImageButtonCreator implements IUICreator{
        /*
            <UIImageButton name=""  pos="">
                <textures/>
                    <normal path=""/> asset/a.png or classpath:asset/a.png or url:https://www.baidu.com/ale.png
                    <cover path=""/>
                    <down path="" />
                </textures/>
                <actions>
                    <donormal action=""/>
                    <docover action=""/>
                    <dodown action=""/>
                </actions>
            </UIImageButton>
        */
        @Override
        public UIActor createUi(Element element){
            String name=element.attributeValue("name");
            Vector pos =createPos(element);
            if(pos==null)
                pos=new Vector(10,10,0,0);
            if(name==null)
                name="DefaultUiName"+ UIActor.IDCreator.createId();
            Element textures=element.element("textures");            
            ITexture normalimage=null,downimage=null,coverimage=null;
            if(textures==null){
                DebugUtil.log("no texture for button defined.!");
                return null;
            }
            Element normal=textures.element("normal");
            if(normal==null){
                DebugUtil.log("no normal texture for button defined.!");                
                return null;
            }
            normalimage=createTexture(normal);
            Element down=textures.element("down");
            if(down!=null){
                downimage=createTexture(down);
            }
            Element cover=textures.element("cover");
            if(cover!=null){
                coverimage=createTexture(cover);
            }
            
            if(normalimage==null){
                DebugUtil.log("normal texture for button creation failed.!");                
                return null;
            }
            
            UIImageButton button=new UIImageButton(name,pos,normalimage,downimage,coverimage);
            Element actions=element.element("actions");
            if(actions!=null){
                Element docover=actions.element("docover");
                Element dodown=actions.element("dodown");
                Element donormal=actions.element("donormal");
                if(docover!=null){
                    IUIAction coveraction=createAction(docover);
                    if(coveraction!=null){
                        button.setToCoverAction(coveraction);
                    }
                }
                if(dodown!=null){
                    IUIAction action=createAction(dodown);
                    if(action!=null){
                        button.setToDownAction(action);
                    }
                }
                if(donormal!=null){
                    IUIAction action=createAction(donormal);
                    if(action!=null){
                        button.setToNormalAction(action);
                    }
                }
            }
            return button;
        }        
        /*
            <name path=""/>
        */
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
            else if(path.startsWith("classpath:")){
                String[] classpaths=path.split(":");
                path=classpaths[1];
                URL url=this.getClass().getClassLoader().getResource(path);
                return tech.createTexture(url);
            }else if(path.startsWith("url:")){
                String[] classpaths=path.split(":");
                path=classpaths[1];
                try {
                    URL url = new URL(path);
                    return tech.createTexture(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            else return tech.createTexture(path);
        }
        /*
            <name action=""/>
        */
        private IUIAction createAction(Element element){
            String action=element.attributeValue("action");
            if(action==null){
                DebugUtil.log("action for button not defined");
                return null;
            }
            IUIAction act=(IUIAction)FactoryUtil.create(action);
            return act;
        }
    }
}
