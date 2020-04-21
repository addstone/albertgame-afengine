package albertgame.afengine.scene.component.render;

import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import albertgame.afengine.scene.SceneCamera;
import albertgame.afengine.util.math.Vector;


/**
 * render for texture,commonly image.<br>
 * @see RenderComponent
 * @author Albert Flex
 */
public class TextureRenderComponent extends RenderComponent{
    
    private ITexture texture;

    public TextureRenderComponent(ITexture texture){
        super();
        this.texture=texture; 
        if(texture!=null){
            super.renderWidth=texture.getWidth();
            super.renderHeight=texture.getHeight();
        }
    }        

    public ITexture getTexture() {
        return texture;
    }

    public void setTexture(ITexture texture) {
        this.texture = texture;
    }
    
    @Override
    public void render(SceneCamera camera,IGraphicsTech tech){
        if(texture==null)return;
        try{
        super.renderWidth=texture.getWidth();
        super.renderHeight=texture.getHeight();
        }catch(Exception ex){}
        
        double ax = super.getRenderX(camera);
        double ay = super.getRenderY(camera);
        Vector r = this.getActor().getTransform().rotation;
        Vector a = this.getActor().getTransform().anchor;
        Vector s = this.getActor().getTransform().scalation;
        
        tech.drawOther("transformtexture",
                new Object[]{texture, (int)ax,(int)ay,
                    a.getX(),a.getY(),s.getX(),s.getY(),(int)r.getZ()});
    }    
}
