package albertgame.afengine.in.parts.input.ui;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.graphics.IColor;
import albertgame.afengine.core.graphics.IFont;
import albertgame.afengine.core.graphics.IGraphicsTech;
import albertgame.afengine.in.parts.input.UIActor;
import albertgame.afengine.core.util.TextUtil;
import albertgame.afengine.core.util.math.Vector;
import albertgame.afengine.core.util.property.StringProperty;
import albertgame.afengine.core.util.property.ValueProperty;
import org.dom4j.Element;

/**
 *
 * @author Admin
 */
public class UITextLabel extends UIActor {

    private StringProperty text;
    private ValueProperty<IColor> colorValue;
    private ValueProperty<IFont> fontValue;
    private boolean hdirty = false;

    public UITextLabel(String name, int x, int y, String text) {
        this(name, new Vector(x, y, 0), new StringProperty(text));
    }

    public UITextLabel(String name, Vector pos, StringProperty text) {
        this(name, pos, text, null, null);
    }

    public UITextLabel(String name, Vector pos, StringProperty text, IFont font, IColor color) {
        super(name, pos);
        this.text = text;
        this.colorValue = new ValueProperty<>(color);
        this.fontValue = new ValueProperty<>(font);
    }

    public StringProperty getText() {
        return text;
    }

    public void setText(StringProperty text) {
        this.text = text;
    }

    public IColor getColor() {
        return colorValue.get();
    }

    public void setColor(IColor color) {
        this.colorValue.set(color);
    }

    public IFont getFont() {
        return fontValue.get();
    }

    public void setFont(IFont font) {
        this.fontValue.set(font);
        hdirty = true;
    }

    @Override
    public void draw(IGraphicsTech tech) {
        if (hdirty) {
            if (fontValue.get() != null && text != null && text.get() != null) {
                super.height = fontValue.get().getFontHeight();
            }
            hdirty = false;
        }
        if (text != null && text.get() != null) {
            if (fontValue.get() == null) {
                fontValue.set(tech.getFont());
            }
            if (colorValue.get() == null) {
                colorValue.set(tech.getColor());
            }
            super.width = fontValue.get().getFontWidth(text.get());

            int dx = super.getUiX();
            int dy = super.getUiY();
            tech.drawText(dx, dy, fontValue.get(), colorValue.get(), text.get());
        }
    }

    public ValueProperty<IColor> getColorValue() {
        return colorValue;
    }

    public void setColorValue(ValueProperty<IColor> colorValue) {
        this.colorValue = colorValue;
    }

    public ValueProperty<IFont> getFontValue() {
        return fontValue;
    }

    public void setFontValue(ValueProperty<IFont> fontValue) {
        this.fontValue = fontValue;
    }

    public static class UITextLabelCreator implements IUICreator {

        /*
            <UITextLabel name pos>
        *      <text></text>
        *      <font path=""></font>
        *      <size></size>
        *      <color></color>                
            </UITextLabel>
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

            UITextLabel label = new UITextLabel(name, pos, text, font, color);
            return label;
        }

        private Vector createPos(Element element) {
            String poss = element.attributeValue("pos");
            String[] posl = poss.split(",");
            double x = Double.parseDouble(posl[0]);
            double y = Double.parseDouble(posl[1]);
            return new Vector(x, y, 0, 0);
        }
    }
}
