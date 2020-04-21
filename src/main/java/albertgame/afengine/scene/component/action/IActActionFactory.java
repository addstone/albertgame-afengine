package albertgame.afengine.scene.component.action;

import albertgame.afengine.scene.component.action.ActionComponent.ActAction;
import java.util.Map;
import org.dom4j.Element;


public interface IActActionFactory {
    ActAction createAction(Element element,Map<String,String> actordatas);
}
