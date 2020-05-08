package albertgame.afengine.input.ui;

import albertgame.afengine.graphics.IDrawStrategy;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.input.InputManager;
import albertgame.afengine.input.UIActor;
import albertgame.afengine.input.UIFace;
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
