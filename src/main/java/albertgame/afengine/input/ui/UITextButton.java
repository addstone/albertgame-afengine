package albertgame.afengine.input.ui;

import albertgame.afengine.app.App;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.graphics.IColor;
import albertgame.afengine.graphics.IFont;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.input.UIActor;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.TextUtil;
import albertgame.afengine.util.math.Vector;
import albertgame.afengine.util.property.StringProperty;
import albertgame.afengine.util.property.ValueProperty;
import org.dom4j.Element;

public class UITextButton extends UIButtonBase {

    private StringProperty text;
    private ValueProperty<IFont> fontValue;
    private ValueProperty<IColor> fontColorValue;

    public UITextButton(String name, int x, int y, String text, IFont font, IColor color) {
        this(name, new Vector(x, y, 0), new StringProperty(text), font, color);
    }

    public UITextButton(String name, int x, int y, String text) {
        this(name, x, y, text, null, null);
    }

    public UITextButton(String name, Vector pos, StringProperty text, IFont font, IColor color) {
        super(name, pos);
        this.text = text;
        this.fontValue = new ValueProperty<IFont>(font);
        this.fontColorValue = new ValueProperty<IColor>(color);
        buttonState = NORMAL;
        if (font != null) {
            super.width = font.getFontWidth(text.get());
            super.height = font.getFontHeight();
        }
    }

    public StringProperty getText() {
        return text;
    }

    public void setText(StringProperty text) {
        this.text = text;
    }

    public IFont getFont() {
        return fontValue.get();
    }

    public void setFont(IFont font) {
        this.fontValue.set(font);
    }

    public IColor getFontColor() {
        return fontColorValue.get();
    }

    public void setFontColor(IColor fontColor) {
        this.fontColorValue.set(fontColor);
    }

    public ValueProperty<IFont> getFontValue() {
        return fontValue;
    }

    public void setFontValue(ValueProperty<IFont> fontValue) {
        this.fontValue = fontValue;
    }

    public ValueProperty<IColor> getFontColorValue() {
        return fontColorValue;
    }

    public void setFontColorValue(ValueProperty<IColor> fontColorValue) {
        this.fontColorValue = fontColorValue;
    }

    //figure out pos of render text.
    //draw text
    @Override
    public void draw(IGraphicsTech tech) {
        if (fontValue.get() == null) {
            fontValue.set(tech.getFont());
        }
        if (fontColorValue.get() == null) {
            fontColorValue.set(tech.getColor());
        }

        super.width = fontValue.get().getFontWidth(text.get());
        super.height = fontValue.get().getFontHeight();

        IFont f = fontValue.get();
        IColor c = fontColorValue.get();
        if (f == null) {
            f = tech.getFont();
            setFont(f);
        }
        if (c == null) {
            c = tech.getColor();
            fontColorValue.set(c);
        }

        int dx = this.getUiX();
        int dy = this.getUiY();

        if (text != null) {
            tech.drawText(dx, dy, f, c, text.get());
        }
    }

    public static class UITextButtonCreator implements IUICreator {

        private final IGraphicsTech tech = ((WindowApp) App.getInstance()).getGraphicsTech();

        /*
            <UITextButton name="" pos="">
        *      <text></text>
        *      <font path=""></font>
        *      <size></size>
        *      <color></color>
                <actions>
                    <donormal action=""/>
                    <dodown action=""/>
                    <docover action=""/>
                </actions>
            </UITextButton>
         */
        @Override
        public UIActor createUi(Element element) {
            String name = element.attributeValue("name");
            Vector pos = createPos(element);
            if (pos == null) {
                pos = new Vector(10, 10, 0, 0);
            }
            if (name == null) {
                name = "DefaultUiName" + IDCreator.createId();
            }

            StringProperty text;
            IFont font;
            IColor color = null;
            IColor backcolor = null;
            Element texte = element.element("text");
            if (texte != null) {
                text = new StringProperty(TextUtil.getRealValue(texte.getText(), null));
            } else {
                text = new StringProperty("DefaultText");
            }
            Element fonte = element.element("font");
            String sizes = element.elementText("size");
            String path = null;
            if (fonte == null) {
                font = ((IGraphicsTech) ((WindowApp) App.getInstance())
                        .getGraphicsTech()).createFont("Dialog",
                                IFont.FontStyle.PLAIN, 30);
            } else if (fonte.attribute("path") != null) {
                path = fonte.attributeValue("path");
                font = ((IGraphicsTech) ((WindowApp) App.getInstance()).
                        getGraphicsTech()).createFontByPath(fonte.getText(), IFont.FontStyle.PLAIN, Integer.parseInt(sizes));
            } else {
                font = ((IGraphicsTech) ((WindowApp) App.getInstance())
                        .getGraphicsTech()).createFont("Dialog",
                                IFont.FontStyle.PLAIN, 30);
            }
            Element colore = element.element("color");
            String colors;

            if (colore == null) {
                colors = IColor.GeneraColor.ORANGE.toString();
            } else {
                colors = element.elementText("color");
            }

            color = ((IGraphicsTech) ((WindowApp) App.getInstance()).
                    getGraphicsTech()).createColor(IColor.GeneraColor.valueOf(colors));

            UITextButton button = new UITextButton(name, pos, text, font, color);
            addActions(button, element.element("actions"));
            return button;
        }

        private Vector createPos(Element element) {
            String poss = element.attributeValue("pos");
            String[] posl = poss.split(",");
            double x = Double.parseDouble(posl[0]);
            double y = Double.parseDouble(posl[1]);
            return new Vector(x, y, 0, 0);
        }

        /*
            <actions>
                
            </actions>
         */
        private void addActions(UITextButton button, Element actions) {
            if (actions != null) {
                Element docover = actions.element("dcover");
                Element dodown = actions.element("dodown");
                Element donormal = actions.element("donormal");
                if (docover != null) {
                    IUIAction coveraction = createAction(docover);
                    if (coveraction != null) {
                        button.setToCoverAction(coveraction);
                    }
                }
                if (dodown != null) {
                    IUIAction action = createAction(dodown);
                    if (action != null) {
                        button.setToDownAction(action);
                    }
                }
                if (donormal != null) {
                    IUIAction action = createAction(donormal);
                    if (action != null) {
                        button.setToCoverAction(action);
                    }
                }
            }
        }

        private IUIAction createAction(Element element) {
            String action = element.attributeValue("action");
            if (action == null) {
                DebugUtil.log("action for button not defined");
                return null;
            }
            IUIAction act = (IUIAction) FactoryUtil.create(action);
            return act;
        }
    }
}
