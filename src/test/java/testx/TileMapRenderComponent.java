/*
 * Copyright 2020 Administrator.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package testx;

import albertgame.afengine.app.App;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import albertgame.afengine.scene.SceneCamera;
import albertgame.afengine.scene.component.render.RenderComponent;
import albertgame.afengine.scene.component.render.RenderComponentFactory.IRenderCreator;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.TextUtil;
import albertgame.afengine.util.XmlUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 *
 * @author Administrator
 */
public class TileMapRenderComponent extends RenderComponent{

    public static class TileImageSet{
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
//                DebugUtil.log("index not in this set.- ["+start+","+end+"] - "+index);
                return null;
            }
            int diff=index-start;
            return tarrays[diff];
        }
        public void setByIndex(int index,ITexture texture){
            if(index<start||index>end){
//                DebugUtil.log("index not in this set.- ["+start+","+end+"] - "+index);
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
    
        
    private List<TileImageSet> imgsetlist;
    private List<int[][]> tileIdMap;    
    
    public TileMapRenderComponent() {
        super();
        tileIdMap=new ArrayList<>();
        imgsetlist=new ArrayList<>();
    }

    @Override
    protected void render(SceneCamera camera, IGraphicsTech tech) {
        int rx=super.getRenderX(camera);
        int ry=super.getRenderY(camera);
        tileIdMap.forEach((map) -> {
            for(int x=0;x!=map.length;++x){
                for(int y=0;y!=map[0].length;++y){
                    TileImageSet set=TileMapRenderCreator.getImageSet(imgsetlist,map[x][y]);
                    if(set!=null){
                        ITexture texture=set.getByIndex(map[x][y]);
                        tech.drawTexture(rx+y*texture.getWidth(),ry+texture.getHeight()*x, texture);
                    }
                }
            }
        });
    }
    

    public List<TileImageSet> getImgsetlist() {
        return imgsetlist;
    }

    public int[][] getTileIdMap(int layer) {
        return tileIdMap.get(layer);
    }    
    
    public static class TileMapRenderCreator implements IRenderCreator{
        public static final String RENDER_NAME="TileMap";
        @Override
        public String getType() {
            return TileMapRenderCreator.RENDER_NAME;
        }

        /*
            <Render type="TileMap">
                <mapfile path=""/>
            </Render>
        */
        @Override
        public RenderComponent create(Element element, Map<String, String> datas){
            DebugUtil.log("render creator - tilemap");
          Element mapfile=element.element("mapfile");
          String path=TextUtil.getRealValue(mapfile.attributeValue("path"),datas);
          Document doc=XmlUtil.readXMLFileDocument(path,false);
          if(doc==null){
              DebugUtil.log("file path for map not right.");
              return null;
          }
          Element root=doc.getRootElement();
            if(root==null||!root.getName().equals("map")){
                DebugUtil.log("map file has no root or root name not map!");
                return null;            
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
            
            TileMapRenderComponent render=new TileMapRenderComponent();
            List<TileImageSet> set=render.getImgsetlist();
            set.addAll(setlist);
            loadTileIdMap(render.tileIdMap,setlist,root,tilewidth,tileheight);
            
            return render;
        }
        public static TileImageSet getImageSet(List<TileImageSet> set,int index){
            for(TileImageSet tile:set){
                if(tile.start<index&&tile.end>index){
                    return tile;
                }
            }
    //        DebugUtil.log("not found texture from map,by index:"+index);
            return null;
        }
        //<tileset firstgid="1" tilewidth="" tileheight=""
        public List<TileImageSet> loadTileSet(Element element){
            List<TileImageSet> set=new ArrayList<>();
            int start=Integer.parseInt(element.attributeValue("firstgid"));
            int tilewidth=Integer.parseInt(element.attributeValue("tilewidth"));
            int tileheight=Integer.parseInt(element.attributeValue("tileheight"));
            DebugUtil.log("start-"+start);
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
            IGraphicsTech tech=((WindowApp)(App.getInstance())).getGraphicsTech();
            String source=image.attributeValue("source");                
            ITexture texture=tech.createTexture(source);
            if(texture==null){
                DebugUtil.log("texture path not correct!");
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
                    ITexture te=texture.cutInstance(i*tilewidth,j*tileheight,
                            tilewidth,tileheight);
                    set.setByIndex(startindex+count,te);
                    DebugUtil.log("load gid:"+(count+startindex));
                    ++count;
                }
            }
            return set;
         }
        
        
        public void loadTileIdMap(List<int[][]> tileidMap,List<TileImageSet> setlist,Element element,int tilewidth,int tileheight){
            Iterator<Element> eiter=element.elementIterator("layer");
            while(eiter.hasNext()){
                Element layer=eiter.next();
                int[][] map=loadLayerMap(setlist,layer,tilewidth,tileheight);
                tileidMap.add(map);
            }
        }
        
        /*
            <layer name="" width="" height="">
                <data>
                  <tile gid=""/>
        */
        public int[][] loadLayerMap(List<TileImageSet> setlist,Element element,int tilewidth,int tileheight){
            Element data=element.element("data");
            int width=Integer.parseInt(element.attributeValue("width"));
            int height=Integer.parseInt(element.attributeValue("height"));
            int[][] map=new int[height][width];
            //第0行，第1列
            int x=0,y=0;
            Iterator<Element> tileiter=data.elementIterator();

            while(tileiter.hasNext()){            
                Element ele=tileiter.next();
                int gid=Integer.parseInt(ele.attributeValue("gid"));
                
                TileImageSet set=getImageSet(setlist,gid);
                if(set!=null){
                    map[x][y]=gid;
                    DebugUtil.log("set gid:["+x+","+y+"]"+gid);
                }
                
                ++y;
                if(y>=width){                
                    y%=width;
                    ++x;
                }                        
            }
            return map;
        }
    }
}