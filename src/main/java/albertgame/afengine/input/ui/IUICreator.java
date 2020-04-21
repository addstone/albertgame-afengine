package albertgame.afengine.input.ui;

import albertgame.afengine.input.UIActor;
import org.dom4j.Element;

/**
 *
 * @author Albert Flex
 */
public interface IUICreator {
    UIActor createUi(Element element);
}
