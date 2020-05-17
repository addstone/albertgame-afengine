package albertgame.afengine.in.parts.input.ui;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.core.graphics.ITexture;
import albertgame.afengine.in.parts.input.UIActor;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.math.Vector;
import java.util.Iterator;
import org.dom4j.Element;

/**
 *
 * @author Albert Flex
 */
public class UIButtonList extends UIPane {

    private int nowindex;
    private int length;

    private boolean isDown;

    public UIButtonList(String name, int x, int y) {
        this(name, new Vector(x, y, 0));
    }

    public UIButtonList(String name, Vector pos) {
        super(name, pos);
        isDown = false;
    }

    public int getNowindex() {
        return nowindex;
    }

    public int getLength() {
        return length;
    }

    public void addUiButton(UIButtonBase button) {
        addChild(button);
        ++length;
    }

    public UIButtonBase getUiButton(int index) {
        return (UIButtonBase) super.children.get(index);
    }

    public void removeUiButton(int index) {
        super.children.remove(index);
        --length;
    }

    public void activeUiButton(int index) {
        UIButtonBase preb = (UIButtonBase) super.children.get(nowindex);
        UIButtonBase button = (UIButtonBase) super.children.get(index);
        if (preb != null && button != null) {
            preb.normal();
            button.cover();
            nowindex = index;
        }
    }

    public void upActive() {
        int nindex = (nowindex + length - 1) % length;
        activeUiButton(nindex);
    }

    public void downActive() {
        int nindex = (nowindex + 1) % length;
        activeUiButton(nindex);
    }

    public static class UIButtonListCreator implements IUICreator {

        /* 
            <UIButtonList name pos back="">
                <UIButton />
                <UIButton />
                <UIButton />
            </UIButtonList>
         */
        @Override
        public UIActor createUi(Element element) {
            String name = element.attributeValue("name");
            Vector pos = createPos(element);
            if (pos == null) {
                pos = new Vector(10, 10, 0, 0);
            }
            if (name == null) {
                name = "DefaultUiName" + UIActor.IDCreator.createId();
            }
            UIButtonList list = new UIButtonList(name, pos);
            Iterator<Element> eleiter = element.elementIterator();
            if (eleiter != null) {
                while (eleiter.hasNext()) {
                    Element ele = eleiter.next();
                    UIButtonBase button = createButton(ele);
                    if (button != null) {
                        list.addUiButton(button);
                    }
                }
            }
            ITexture back = createTexture(element);
            if (back != null) {
                list.setBack(back);
            }
            return list;
        }

        private UIButtonBase createButton(Element element) {
            String name = element.getName();
            if (name.equals("UIImageButton")) {
                IUICreator creator = new UIImageButton.UIImageButtonCreator();
                return (UIButtonBase) creator.createUi(element);
            } else if (name.equals("UITextButton")) {
                IUICreator creator = new UITextButton.UITextButtonCreator();
                return (UIButtonBase) creator.createUi(element);
            } else {
                DebugUtil.log("not type for button, in buttonlist");
                return null;
            }
        }

        private Vector createPos(Element element) {
            String poss = element.attributeValue("pos");
            String[] posl = poss.split(",");
            double x = Double.parseDouble(posl[0]);
            double y = Double.parseDouble(posl[1]);
            return new Vector(x, y, 0, 0);
        }
        private final IGraphicsTech tech = ((WindowApp) App.getInstance()).getGraphicsTech();

        private ITexture createTexture(Element element) {
            String path = element.attributeValue("back");
            if (path == null) {
                DebugUtil.log("path for texture is not defined.return null texture");
                return null;
            } else {
                return tech.createTexture(path);
            }
        }

    }
}
