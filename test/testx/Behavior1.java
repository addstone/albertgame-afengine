package testx;

import afengine.component.behavior.ActorBehavior;
import afengine.component.render.TextureRenderComponent;
import afengine.core.AppState;
import afengine.core.WindowApp;
import afengine.core.util.Debug;
import afengine.core.util.IDCreator;
import afengine.core.util.XMLEngineBoot;
import afengine.core.window.IGraphicsTech;
import afengine.core.window.ITexture;
import afengine.part.scene.Actor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;

public class Behavior1 extends ActorBehavior{
    
    public class TileImageSet{
        private int start;
        private int end;
        private int tilewidth,tileheight;

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
        
        ITexture[] tarrays;
        public TileImageSet(int start,int end,int tilewidth,int tileheight){
            this.start=start;
            this.end=end;
            tarrays=new ITexture[end-start];
            this.tilewidth=tilewidth;
            this.tileheight=tileheight;
        }
        public ITexture getByIndex(int index){
            if(index<start||index>end){
//                Debug.log("index not in this set.- ["+start+","+end+"] - "+index);
                return null;
            }
            int diff=index-start;
            return tarrays[diff];
        }
        public void setByIndex(int index,ITexture texture){
            if(index<start||index>end){
//                Debug.log("index not in this set.- ["+start+","+end+"] - "+index);
                return;
            }
            int diff=index-start;
            tarrays[diff]=texture;
        }

        public int getTilewidth() {
            return tilewidth;
        }

        public int getTileheight() {
            return tileheight;
        }        
    }
    public void loadMap(String path,Actor mapactor){
        Document doc=XMLEngineBoot.getXMLWritableDocument(path);
        if(doc==null){
            Debug.log("map path no correct!");
            return;
        }
        
        Element root=doc.getRootElement();
        if(root==null||!root.getName().equals("map")){
            Debug.log("map file has no root or root name not map!");
            return;            
        }                
        
        int tilewidth=Integer.parseInt(root.attributeValue("tilewidth"));
        int tileheight=Integer.parseInt(root.attributeValue("tileheight"));
        
        List<TileImageSet> setlist=new ArrayList<>();
        Iterator<Element> eiter=root.elementIterator("tileset");
        while(eiter.hasNext()){
            Element tileset=eiter.next();
            List<TileImageSet> set=loadTileSet(tileset);
            setlist.addAll(set);
        }
        
        eiter=root.elementIterator("layer");
        while(eiter.hasNext()){
            Element layer=eiter.next();
            Actor layeractor=loadLayer(layer,setlist,tilewidth,tileheight);
            mapactor.addChild(layeractor);
        }        
    }    
    public TileImageSet getImageSet(List<TileImageSet> set,int index){
        for(TileImageSet tile:set){
            if(tile.start<index&&tile.end>index){
                return tile;
            }
        }
//        Debug.log("not found texture from map,by index:"+index);
        return null;
    }
    //<tileset firstgid="1" tilewidth="" tileheight=""
    public List<TileImageSet> loadTileSet(Element element){
        List<TileImageSet> set=new ArrayList<>();
        int start=Integer.parseInt(element.attributeValue("firstgid"));
        int tilewidth=Integer.parseInt(element.attributeValue("tilewidth"));
        int tileheight=Integer.parseInt(element.attributeValue("tileheight"));
        Debug.log("start-"+start);
        Iterator<Element> eiter=element.elementIterator();
        while(eiter.hasNext()){
            Element image=eiter.next();
            TileImageSet se=loadImageSet(image,start,tilewidth,tileheight);
            if(se!=null){
                set.add(se);
            }
        }        
        return set;
    }
    
    //<image source="" width="" height=""/>
    public TileImageSet loadImageSet(Element image,int startindex,int tilewidth,int tileheight){
        IGraphicsTech tech=((WindowApp)(AppState.getRunningApp())).getGraphicsTech();
        String source=image.attributeValue("source");                
        ITexture texture=tech.createTexture(source);
        if(texture==null){
            Debug.log("texture path not correct!");
            return null;
        }
        int imagewidth=Integer.parseInt(image.attributeValue("width"));
        int imageheight=Integer.parseInt(image.attributeValue("height"));
        int ix=imagewidth/tilewidth;
        int iy=imageheight/tileheight;
        int end=ix*iy;
        TileImageSet set=new TileImageSet(startindex,startindex+end,tilewidth,tileheight);
        int count=0;
        for(int j=0;j!=iy;++j){
            for(int i=0;i!=ix;++i){
                ITexture te=texture.getCutInstance(i*tilewidth,j*tileheight,
                        tilewidth,tileheight);
                set.setByIndex(startindex+count,te);
                ++count;
            }
        }
        return set;
    }
    
    
    /*
        <layer name="" width="" height="">
            <data>
              <tile gid=""/>
    */
    public Actor loadLayer(Element element,List<TileImageSet> set,int tilewidth,int tileheight){
        Element data=element.element("data");
        int width=Integer.parseInt(element.attributeValue("width"));
        int height=Integer.parseInt(element.attributeValue("height"));
        //第0行，第1列
        int x=0,y=0;
        Iterator<Element> tileiter=data.elementIterator();
        Actor layeractor=new Actor(element.attributeValue("name"));
        
        while(tileiter.hasNext()){            
            Element ele=tileiter.next();
            int gid=Integer.parseInt(ele.attributeValue("gid"));
            int px=x*tilewidth;
            int py=y*tileheight;
            
            Actor actor=createTile(set,gid,px,py);
            if(actor!=null){
                layeractor.addChild(actor);
            }
            
            ++x;
            if(x>=width){                
                x%=width;
                ++y;
            }                        
        }
        
        return layeractor;
    }
    
    private Actor createTile(List<TileImageSet> sets,int index,int x,int y){
        TileImageSet set=getImageSet(sets,index);
        if(set==null){
//            Debug.log("not found image by index:"+index);
            return null;
        }
        Actor actor=new Actor("tile"+index+"-"+IDCreator.createId());
        actor.getTransform().position.setX(x);
        actor.getTransform().position.setY(y);
        ITexture render=set.getByIndex(index);
        TextureRenderComponent r=new TextureRenderComponent(render);
        actor.addComponent(r, true);        
        return actor;
    }
    
    @Override
    public void toWake(){
        Actor actor=super.behaviorbean.getActor();
        Debug.log("actor is null?"+(actor==null));
        loadMap("test/testx/map.tmx",actor);
        Debug.log("toWake Behavior");
    }        
    @Override
    public void update(long time){}    
}
