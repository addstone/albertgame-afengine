package albertgame.afengine.in.core.input.ui;

import albertgame.afengine.core.input.UIActor;
import org.dom4j.Element;

/**
 *
 * @author Albert Flex
 */
public interface IUICreator {
    UIActor createUi(Element element);
}
