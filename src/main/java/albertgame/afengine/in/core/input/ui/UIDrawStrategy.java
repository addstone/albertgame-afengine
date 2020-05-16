package albertgame.afengine.in.core.input.ui;

import albertgame.afengine.core.graphics.IDrawStrategy;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.input.InputManager;
import albertgame.afengine.core.input.UIActor;
import albertgame.afengine.core.input.UIFace;

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
            drawFace(face,tech);
        });
        
        UIFace popupFace=center.getPopupFace();
        if(popupFace!=null){
            drawFace(popupFace,tech);
        }
    }   
    private void drawFace(UIFace face,IGraphicsTech tech){
        List<UIActor> uilist=face.getActorList();
        uilist.forEach((ui) -> {
            ui.draw(tech);
        });
    }
}
