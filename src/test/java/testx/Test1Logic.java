package testx;

import albertgame.afengine.core.app.AppBoot;
import albertgame.afengine.core.app.IAppLogic;
import albertgame.afengine.core.message.Message;
import albertgame.afengine.core.message.Message.IHandler;
import albertgame.afengine.core.input.InputServlet;
import albertgame.afengine.in.parts.scene.Actor;
import albertgame.afengine.in.parts.scene.Scene;
import albertgame.afengine.in.parts.scene.SceneManager;
import albertgame.afengine.in.components.render.RenderComponent;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.FactoryUtil;
import albertgame.afengine.in.components.render.RenderComponentFactory;

/**
 *
 * @author Admin
 */
public class Test1Logic implements IAppLogic{

    public static void main(String[] args) {
        FactoryUtil.initEngineFactory();
        AppBoot.boot("test/testx/testx-boot1.xml");
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
//        Scene scene=SceneManager.getInstance().getRunningScene();
//        Document doc=XmlUtil.readXMLFileDocument("test/testx/test1-scene-cp.xml",true);
//        Element root=doc.getRootElement();
//        SceneFileHelp.outputSceneToXML(scene,root);
//        XmlUtil.writeXMLFile("test/testx/test1-scene-cp.xml", doc);
        return true;
    }    
    
    public static class MoveHandler implements IHandler{
        private Actor actor;
        private Actor map;
        @Override
        public boolean handle(Message msg){                        
            if(actor==null){
                Scene scene=SceneManager.getInstance().getRunningScene();
                actor=scene.findActorByName("player");
                map=scene.findActorByName("map-content");
            }                        
            
            double oldx=actor.getTransform().position.getX();
            double oldy=actor.getTransform().position.getY();            
            
            int  keycode=(int)msg.extraObjs[0];
            switch (keycode) {
                case (int)InputServlet.KeyCode_Left:
                    {
                        double x=actor.getTransform().position.getX();
                        actor.getTransform().position.setX(x-32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),x-32,oldy)){
                            DebugUtil.warning("you can not walk this way!");
                            actor.getTransform().position.setX(x);
                        }       break;
                    }
                case (int)InputServlet.KeyCode_Right:
                    {
                        double x=actor.getTransform().position.getX();
                        actor.getTransform().position.setX(x+32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),x+32,oldy)){
                            DebugUtil.warning("you can not walk this way!");
                            actor.getTransform().position.setX(x);
                        }       break;
                    }
                case (int)InputServlet.KeyCode_Up:
                    {
                        double y=actor.getTransform().position.getY();
                        actor.getTransform().position.setY(y-32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),oldx,y-32)){
                            DebugUtil.warning("you can not walk this way!");
                            actor.getTransform().position.setY(y);
                        }       break;
                    }
                case (int)InputServlet.KeyCode_Down:
                    {
                        double y=actor.getTransform().position.getY();
                        actor.getTransform().position.setY(y+32);
                        if(isKabe((TileMapRenderComponent)map.getComponent(RenderComponent.COMPONENT_NAME),oldx,y+32)){
                            DebugUtil.warning("you can not walk this way!");
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
            DebugUtil.log("gid:"+gid);
            return gid==0;
        }
    }
}
