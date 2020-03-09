package testx;

import afengine.component.render.RenderComponent;
import afengine.core.IAppLogic;
import afengine.core.util.Debug;
import afengine.core.util.XMLEngineBoot;
import afengine.part.message.IMessageHandler;
import afengine.part.message.Message;
import afengine.part.scene.Actor;
import afengine.part.scene.Scene;
import afengine.part.scene.SceneCenter;
import afengine.part.scene.SceneFileHelp;
import java.awt.event.KeyEvent;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 *
 * @author Admin
 */
public class Test1Logic implements IAppLogic{

    public static void main(String[] args) {
        XMLEngineBoot.bootEngine("test/testx/testx-boot1.xml");
    }
    
    @Override
    public boolean init() {
        return true;
    }
    

    @Override
    public boolean update(long time) {
        return true;
    }

    @Override
    public boolean shutdown(){       
        Scene scene=SceneCenter.getInstance().getRunningScene();
        Document doc=XMLEngineBoot.getXMLWritableDocument("test/testx/test1-scene-cp.xml");
        Element root=doc.getRootElement();
        SceneFileHelp.outputSceneToXML(scene,root);
        XMLEngineBoot.writeXMLFile("test/testx/test1-scene-cp.xml", doc);
        return true;
    }    
    
    public static class MoveHandler implements IMessageHandler{
        private Actor actor;
        private Actor map;
        @Override
        public boolean handle(Message msg){                        
            if(actor==null){
                Scene scene=SceneCenter.getInstance().getRunningScene();
                actor=scene.findActorByName("player");
                map=scene.findActorByName("map-content");
            }                        
            Debug.log("handler----");
            
            double oldx=actor.getTransform().position.getX();
            double oldy=actor.getTransform().position.getY();            
            
            KeyEvent key=(KeyEvent)msg.extraObjs[0];
            switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    {
                        double x=actor.getTransform().position.getX();
                        actor.getTransform().position.setX(x-32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),x-32,oldy)){
                            actor.getTransform().position.setX(x);
                        }       break;
                    }
                case KeyEvent.VK_RIGHT:
                    {
                        double x=actor.getTransform().position.getX();
                        actor.getTransform().position.setX(x+32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),x+32,oldy)){
                            actor.getTransform().position.setX(x);
                        }       break;
                    }
                case KeyEvent.VK_UP:
                    {
                        double y=actor.getTransform().position.getY();
                        actor.getTransform().position.setY(y-32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),oldx,y-32)){
                            actor.getTransform().position.setY(y);
                        }       break;
                    }
                case KeyEvent.VK_DOWN:
                    {
                        double y=actor.getTransform().position.getY();
                        actor.getTransform().position.setY(y+32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),oldx,y+32)){
                            actor.getTransform().position.setY(y);
                        }       break;
                    }
                default:
                    break;
            }
            return false;
        }        
        
        public boolean isKabe(TileMapRenderComponent comp,double destplayerx,double destplayery){
            int indexx=(int) (destplayerx/32);
            int indexy=(int) (destplayery/32);
            int gid=comp.getTileIdMap(3)[indexy][indexx];
            Debug.log("gid:"+gid);
            return gid==0;
        }
    }
}
