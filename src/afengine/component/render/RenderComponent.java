package afengine.component.render;

import afengine.core.AppState;
import afengine.core.WindowApp;
import afengine.core.util.Debug;
import afengine.core.util.Vector;
import afengine.core.window.IGraphicsTech;
import afengine.part.scene.Actor;
import afengine.part.scene.ActorComponent;
import afengine.part.scene.SceneCamera;
import java.util.Comparator;

/**
 * base class for rendercomponent.<br>
 * some render component should override the render method for use,<br>
 * and you chould use beforerender,and afterrender<br>
 * while you should add any drawstrategy to graphicstech,to call render component.renderComponent by hand<br>
 * @author Albert Flex
 */
public class RenderComponent extends ActorComponent{
    public static final String COMPONENT_NAME="Render";
    

    private static Comparator<Actor> comparator=(Actor o1, Actor o2) -> {
        RenderComponent r1=(RenderComponent) o1.getComponent(RenderComponent.COMPONENT_NAME);
        RenderComponent r2=(RenderComponent) o2.getComponent(RenderComponent.COMPONENT_NAME);
        if(r1==null||r2==null)return 0;
        
        int diff=r1.getRenderOrder()-r2.getRenderOrder();
        if(diff>0){
            return 1;
        }else if(diff<0)
            return -1;
        else return 0;
    };;

    public static Comparator<Actor> getComparator() {
        return comparator;
    }

    public static void setComparator(Comparator<Actor> comparator) {
        RenderComponent.comparator = comparator;
    }
    
    public static final Comparator<Actor> orderComparator=(Actor o1, Actor o2) -> {
        RenderComponent r1=(RenderComponent) o1.getComponent(RenderComponent.COMPONENT_NAME);
        RenderComponent r2=(RenderComponent) o2.getComponent(RenderComponent.COMPONENT_NAME);
        if(r1==null||r2==null)return 0;
        
        int diff=r1.getRenderOrder()-r2.getRenderOrder();
        if(diff>0){
            return 1;
        }else if(diff<0)
            return -1;
        else return 0;
    };
    
    
    public RenderComponent() {
        super(RenderComponent.COMPONENT_NAME);
        renderOrder=0;
    }    
            
    protected int renderWidth,renderHeight;
    private int renderOrder;
    
    protected void beforeRender(SceneCamera camera,IGraphicsTech tech){}
    /*
        need override.
    */
    protected void render(SceneCamera camera,IGraphicsTech tech){}

    public final void renderComponent(SceneCamera camera,IGraphicsTech tech){
        beforeRender(camera,tech);
        render(camera,tech);
        afterRender(camera,tech);
    }

    protected void afterRender(SceneCamera camera,IGraphicsTech tech){}
    
    public boolean isPointIn(SceneCamera camera,Vector point){
        double ax=getRenderX(camera);
        double ay=getRenderY(camera);
        Vector a = this.getActor().getTransform().anchor;
        float width = this.renderWidth;
        float height=this.renderHeight;
        float dx = (float) (ax-width*a.getX());
        float dy = (float) (ay-height*a.getY());
        float px = (float) ax;
        float py = (float) ay;
        if(px<dx||px>(dx+width))return false;
        
        return !(py<dy||py>(dy+height));
    }
    public int getRenderX(SceneCamera camera){
        double dx = super.getActor().getAbsoluteX();
        double cx = camera.getPos().getX();
        double ox = camera.getWidthOffSize();
        WindowApp wapp=(WindowApp)(AppState.getRunningApp());
        if(wapp==null){
            Debug.log("get render x failed:not a window app");
            System.exit(0);
        }
        IGraphicsTech tech=wapp.getGraphicsTech();
        int winwidth=tech.getWindowWidth();
        double offx=dx-cx;
        return (int)(offx+winwidth*ox);
    }
    public int getRenderY(SceneCamera camera){
        double dy = super.getActor().getAbsoluteY();
        double cy = camera.getPos().getY();
        double oy = camera.getHeightOffSize();
        WindowApp wapp=(WindowApp)(AppState.getRunningApp());
        if(wapp==null){
            Debug.log("get render x failed:not a window app");
            System.exit(0);
        }
        IGraphicsTech tech=wapp.getGraphicsTech();
        int winheight=tech.getWindowHeight();
        double offy=dy-cy;
        return (int)(offy+winheight*oy);        
    }

    public int getRenderWidth() {
        return renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }    

    public int getRenderOrder() {
        return renderOrder;
    }

    public void setRenderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
    }
    public boolean isInScreen(SceneCamera camera,IGraphicsTech tech){
        int dx=this.getRenderX(camera);
        int dy=this.getRenderY(camera);
        int dxx=dx+this.getRenderWidth();
        int dyy=dy+this.getRenderHeight();
        int screenwidth=tech.getWindowWidth();
        int screenheight=tech.getWindowHeight();
        return !(dxx<0||dx>screenwidth||dyy<0||dy>screenheight);
    }
}
