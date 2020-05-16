package albertgame.afengine.in.components.action;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.graphics.ITexture;
import albertgame.afengine.in.parts.scene.Actor;
import albertgame.afengine.in.components.render.RenderComponent;
import albertgame.afengine.in.components.render.TextureRenderComponent;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.TextUtil;
import albertgame.afengine.core.util.XmlUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;

public class SpriteAction extends ActionComponent.ActAction {
    public SpriteAction(String name,boolean repeat) {
        super(name);
        shapeList=new ArrayList<>();
        this.repeat=repeat;
    }

    
    @Override
    public boolean isEnd() {
        return isEnd;
    }

    @Override
    public void update(long time) {
        updateAnimate(time);
        ITexture nowTexture = getTexture();
        Actor actor = super.actionComp.getActor();
        TextureRenderComponent render = (TextureRenderComponent) actor.getComponent(RenderComponent.COMPONENT_NAME);
        if(render==null){
            render = new TextureRenderComponent(nowTexture);
            actor.addComponent(render,false);
            render.awake();
        }        
        else{
            render.setTexture(nowTexture);
        }        
    }    
    
    private boolean repeat=false;
    
    private final List<AnimateShape> shapeList;
    private int index;   
    private  int size;

    private long totaltime;
    private long updatetime;
    private boolean isEnd;
           
    public int getSize()
    {
        return size;
    }
    public ITexture getTexture()
    {
        return shapeList.get(index).texture;
    }
    
    public void addAnimate(ITexture texture,long delttime)
    {
        totaltime+=delttime;
        ++size;
        shapeList.add(new AnimateShape(texture,totaltime));
    }
    private class AnimateShape
    {
        ITexture texture;
        long endtime;
        public AnimateShape(ITexture t,long time)
        {
            texture=t;
            endtime=time;
        }
    }    

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    @Override
    public void start()
    {
        updatetime=0;
        index=0;
        isEnd=false;
    }

    private void updateAnimate(long time)
    {
        if(isEnd)return;
        
        if (shapeList.size() > 0) 
        {
            updatetime += time;

            if (updatetime >= totaltime) 
            {
                updatetime = updatetime % totaltime;
                index = 0;
                if(repeat){
                    isEnd=false;                    
                }
                else{
                    isEnd=true;
                }
            }
            while (updatetime > shapeList.get(index).endtime) 
            {
                index++;
            }
        }
    }    
    
    public static class SpriteActionFactory implements IActActionFactory{
        
        /*
            <act type="SpriteAction" name="" path="*.xml" /> 
        */
        @Override
        public ActionComponent.ActAction createAction(Element element, Map<String,String> actordatas) {
            String path = element.attributeValue("path");            
            if(path!=null){
                path=TextUtil.getRealValue(path, actordatas);
                Document doc = XmlUtil.readXMLFileDocument(path,false);
                Element root = doc.getRootElement();
                if(root!=null&&root.getName().equals("sprite-action")){
                        String name=element.attributeValue("name");
                        name=TextUtil.getRealValue(name, actordatas);
                        String repeat=element.attributeValue("repeat");
                        boolean re=false;
                        if(repeat!=null&&repeat.equals("true")){
                            re=true;
                        }
                        SpriteAction sprite = createSprite(root,name,re,actordatas);
                        return sprite;
                }
                DebugUtil.log("sprite action create failed.");
            }else{
                    DebugUtil.log("xml error or root is not sprite-action");
                    return null;
            }
            
            DebugUtil.log("path is not found.");
            return null;
        }   
        
        /*
            <sprite-action>                
                <frame texture="" cord="x,y,w,h" deltetime=""/>
                <frame texture="" cord="" deltetime=""/>
                <frame texture="" cord="" deltetime=""/>
                <frame texture="" cord="" deltetime=""/>                
            </sprite-action>
        */
        public static SpriteAction createSprite(Element actRoot,String name,boolean repeat,Map<String,String> actordatas){
            
            SpriteAction sprite=new SpriteAction(name,repeat);
            Iterator<Element> frameiter = actRoot.elementIterator();
            while(frameiter.hasNext()){
                Element frame = frameiter.next();
                addFrame(sprite,frame,actordatas);
            }
            return sprite;
        }
        /*
            you can not use #value,must use value.
            <frame texture"" cord="x,y,w,h" delttime=""/>
        */
        private static final Map<String,ITexture> textureMap=new HashMap<>();
        private static void addFrame(SpriteAction sprite,Element frame,Map<String,String> actordatas){
            String texture=frame.attributeValue("texture");
            if(texture!=null){
                texture=TextUtil.getRealValue(texture, actordatas);
                WindowApp wapp = (WindowApp)(App.getInstance());
                if(wapp==null){
                    DebugUtil.log("it's not a windowapp, you can not create sprite action.");
                }else{                    
                    IGraphicsTech tech = wapp.getGraphicsTech();
                    ITexture text=null;
                    text=textureMap.get(texture);
                    String delttime = frame.attributeValue("delttme");
                    long delt=100;
                    if(delttime!=null){
                        delt = Long.parseLong(delttime);
                    }
                    if(text!=null){
                        sprite.addAnimate(text, delt);                                                                        
                    }
                    if(text==null){
                        String cord = frame.attributeValue("cord");
                        if(cord==null)
                            text = tech.createTexture(texture);
                        else{
                            String[] cords = cord.split(",");                        
                            if(cords.length==4){
                                text=tech.createTexture(texture,
                                        Integer.parseInt(cords[0]),
                                        Integer.parseInt(cords[1]),
                                        Integer.parseInt(cords[2]),
                                        Integer.parseInt(cords[3]));
                            }else{
                                text=tech.createTexture(texture);
                            }
                        }
                    if(text!=null){
                        textureMap.put(texture, text);
                        sprite.addAnimate(text, delt);                                                
                    }
                }
               if(text==null){
                        DebugUtil.log("texture create failed.");
               }   
                }
            }
        }
    }
}
