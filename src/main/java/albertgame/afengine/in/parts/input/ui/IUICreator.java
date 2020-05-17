package albertgame.afengine.in.parts.input.ui;

import albertgame.afengine.in.parts.input.UIActor;
import org.dom4j.Element;

/**
 *
 * @author Albert Flex
 */
public interface IUICreator {
    UIActor createUi(Element element);
}
