package albertgame.afengine.in.components.action;

import java.util.Map;
import org.dom4j.Element;


public interface IActActionFactory {
    ActionComponent.ActAction createAction(Element element, Map<String,String> actordatas);
}
