package albertgame.afengine.in.parts.input.ui;

import albertgame.afengine.core.graphics.IColor;
import albertgame.afengine.core.graphics.IDrawStrategy;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.graphics.IGraphicsWindow;
import albertgame.afengine.in.parts.input.InputManager;
import albertgame.afengine.in.parts.input.UIActor;
import albertgame.afengine.in.parts.input.UIFace;

import java.util.List;

public class UIDrawStrategy implements IDrawStrategy{
    private final InputManager center;
    public UIDrawStrategy(){
        center=InputManager.getInstance();
    }
    
    //先绘制激活的ui
    //再绘制popupFace
    @Override
    public void draw(IGraphicsTech tech) {

        List<UIFace> faceList=center.getActivedfaceList();
        faceList.forEach((face) -> {
            drawFace(face,tech,null);
        });
        
        UIFace popupFace=center.getPopupFace();
        if(popupFace!=null){
            drawFace(popupFace,tech,center.getPopupbackColor());
        }
    }   
    private void drawFace(UIFace face,IGraphicsTech tech,IColor popback){        
        if(popback!=null){
            int w=IGraphicsWindow.window().getWindowWidth();
            int h=IGraphicsWindow.window().getWindowHeight();
            IColor oldc=tech.getColor();
            tech.setColor(popback);
            tech.drawRoundRect(0, 0, w, h, 0,0, true);
            tech.setColor(oldc);
        }
        List<UIActor> uilist=face.getActorList();
        uilist.forEach((ui) -> {
            ui.draw(tech);
        });
    }    
}
