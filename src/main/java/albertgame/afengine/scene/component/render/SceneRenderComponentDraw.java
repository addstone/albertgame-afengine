package albertgame.afengine.scene.component.render;

import albertgame.afengine.graphics.IDrawStrategy;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.scene.Actor;
import albertgame.afengine.scene.Scene;
import albertgame.afengine.scene.SceneCamera;
import albertgame.afengine.scene.SceneManager;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Albert Flex
 */
public class SceneRenderComponentDraw implements IDrawStrategy{
    private final SceneManager sceneCenter=SceneManager.getInstance();        
        
   @Override
    public void draw(IGraphicsTech tech) {
        Scene scene=sceneCenter.getRunningScene();
        List<Actor> actormap=scene.rootList;
        Iterator<Actor> entryiter=actormap.iterator();
        while(entryiter.hasNext()){
            Actor actor=entryiter.next();
            updateOrder(actor);
            renderActor(actor,tech,scene.getCamera());
        }
    }
    private void renderActor(Actor actor,IGraphicsTech tech,SceneCamera camera){
        RenderComponent render=(RenderComponent) actor.getComponent(RenderComponent.COMPONENT_NAME);
        if(render!=null&&render.isInScreen(camera, tech)&&render.isActive()){
            render.renderComponent(camera, tech);
        }                
        List<Actor> children=actor.getChildren();        
        children.sort(RenderComponent.getComparator());
        children.forEach((ac) -> {
            renderActor(ac,tech,camera);
        });                
    }    
    private void updateOrder(Actor actor){
        List<Actor> children=actor.getChildren();                
        children.sort(RenderComponent.getComparator());        
        children.forEach((act)->{
            updateOrder(act);
        });
    }
}
