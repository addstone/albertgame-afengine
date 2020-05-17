package albertgame.afengine.in.components.render;

import albertgame.afengine.core.graphics.IDrawStrategy;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.in.parts.scene.Actor;
import albertgame.afengine.in.parts.scene.SceneManager;
import albertgame.afengine.in.parts.scene.Scene;
import albertgame.afengine.in.parts.scene.SceneCamera;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Albert Flex
 */
public class SceneRenderComponentDraw implements IDrawStrategy {

    private final SceneManager sceneCenter = SceneManager.getInstance();

    @Override
    public void draw(IGraphicsTech tech) {
        Scene scene = sceneCenter.getRunningScene();
        List<Actor> actormap = scene.rootList;
        Iterator<Actor> entryiter = actormap.iterator();
        try {
            while (entryiter.hasNext()) {
                Actor actor = entryiter.next();
                renderActor(actor, tech, scene.getCamera());
            }
        }catch (Exception ex){

        }
    }

    private void renderActor(Actor actor, IGraphicsTech tech, SceneCamera camera) {
        RenderComponent render = (RenderComponent) actor.getComponent(RenderComponent.COMPONENT_NAME);
        if (render != null && render.isInScreen(camera, tech) && render.isActive()) {
            render.renderComponent(camera, tech);
        }
        List<Actor> children = actor.getChildren();
        children.forEach((ac) -> {
            renderActor(ac, tech, camera);
        });
    }
//
//    private void updateOrder(Actor actor) {
//        List<Actor> children = actor.getChildren();
//        children.sort(RenderComponent.getComparator());
//        children.forEach((act) -> {
//            updateOrder(act);
//        });
//    }
}
