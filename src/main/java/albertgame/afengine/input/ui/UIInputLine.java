package albertgame.afengine.input.ui;

import albertgame.afengine.app.App;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.app.message.Message;
import albertgame.afengine.graphics.IColor;
import albertgame.afengine.graphics.IFont;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import albertgame.afengine.input.InputServlet;
import albertgame.afengine.input.UIActor;
import albertgame.afengine.input.UIFace;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.math.Vector;
import albertgame.afengine.util.property.StringProperty;
import albertgame.afengine.util.property.ValueProperty;
import org.dom4j.Element;

/**
 *
 * @author Admin
 */
public class UIInputLine extends UIActor {

    private char[] texts;
    private final int length;
    private ValueProperty<IColor> colorValue,curColorValue;
    private ValueProperty<IFont> fontValue;    
    private StringProperty placeHolder = new StringProperty("Input Something...");
    private int curpos;
    private boolean dirty = false;
    private char secretput = '*';
    private boolean secret = false;
    private ValueProperty<ITexture> backValue;

    public UIInputLine(String name, int x, int y, int length, IColor color, IFont font) {
        this(name, new Vector(x, y, 0), length, color, font);
    }

    public UIInputLine(String name, Vector pos, int length, IColor color, IFont font) {
        super(name, pos);
        texts = new char[length];
        this.length = length;
        this.colorValue = new ValueProperty<>(color);
        this.fontValue = new ValueProperty<>(font);
        this.backValue = new ValueProperty<>();
        this.curColorValue=new ValueProperty<>();
        this.dirty=true;
        curpos = 0;
    }

    public void append(char word) {
        if (curpos >= length) {
            DebugUtil.log("no more capacity for append char word.");
            return;
        }

        texts[curpos] = word;
        ++curpos;
        dirty = true;
    }

    public char back() {
        if (curpos <= 0) {
            DebugUtil.log("no more capacity for append char word.");
            return (char) -1;
        }

        --curpos;
        dirty = true;
        return texts[curpos];
    }

    public String getText() {
        return String.copyValueOf(texts, 0, curpos);
    }

    public void setText(String text) {
        for (int i = 0; i != length && i != text.length(); ++i) {
            char word = text.charAt(i);
            texts[curpos] = word;
            ++curpos;
        }
        dirty = true;
    }

    public ValueProperty<IColor> getColorValue() {
        return colorValue;
    }

    public void setColorValue(ValueProperty<IColor> colorValue) {
        this.colorValue = colorValue;
    }

    public StringProperty getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(StringProperty placeHolder) {
        this.placeHolder = placeHolder;
    }

    public void enableSecretMode(boolean enable) {
        secret = enable;
    }

    public char getSecretput() {
        return secretput;
    }

    public void setSecretput(char secretput) {
        this.secretput = secretput;
    }

    public int getTextLength() {
        return curpos;
    }

    public int getTextCapacity() {
        return length;
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
        dirty = true;
    }

    private boolean showsplit = false;
    private boolean isOn = true;
    private long t;

    public ITexture getBack() {
        return backValue.get();
    }

    public void setBack(ITexture texture) {
        backValue.set(texture);
    }

    public ValueProperty<ITexture> getBackValue() {
        return backValue;
    }

    public void setBackValue(ValueProperty<ITexture> backValue) {
        this.backValue = backValue;
    }

    public ValueProperty<IColor> getCurColorValue() {
        return curColorValue;
    }

    public void setCurColorValue(ValueProperty<IColor> curColorValue) {
        this.curColorValue = curColorValue;
    }

    long lasttime = System.currentTimeMillis();

    @Override
    public void draw(IGraphicsTech tech) {
        long nowtime = System.currentTimeMillis();
        long time = nowtime - lasttime;
        lasttime = nowtime;
        t += time;

        if (backValue.get() != null) {
            super.width = backValue.get().getWidth();
            super.height = backValue.get().getHeight();
        }
        if (fontValue.get() != null && dirty) {
            super.width = fontValue.get().getFontWidth("WP")*this.length;
            super.height = fontValue.get().getFontHeight();
            dirty = false;
        }

        if (t > 1000) {
            t = 0;
        }

        if (t > 0 && t < 600) {
            showsplit = true;
        } else {
            showsplit = false;
        }

        if (fontValue.get() == null) {
            fontValue.set(tech.getFont());
        }
        if (colorValue.get() == null) {
            colorValue.set(tech.getColor());
        }
        if(curColorValue.get()==null){
            curColorValue.set(tech.createColor(IColor.GeneraColor.WHITE));
        }
        int dx = super.getUiX();
        int dy = super.getUiY();
        if (backValue.get() != null) {
            tech.drawTexture(dx, dy, backValue.get());
            dy = dy + backValue.get().getHeight() / 2 - fontValue.get().getFontHeight() / 2;
            dx += 3;
        }

        String text = null;
        if (secret) {
            int size = getText().length();
            char[] chars = new char[size];
            for (int i = 0; i != size; ++i) {
                chars[i] = secretput;
            }
            text = String.copyValueOf(chars);
        } else {
            text = getText();
            if (text.length() == 0) {
                text = placeHolder.get();
            }
        }
        tech.drawText(dx, dy, fontValue.get(), colorValue.get(), text);
        int dxx = dx + fontValue.get().getFontWidth(text)+1;
        if (isOn && showsplit) {
            IColor oldc=tech.getColor();
            tech.setColor(tech.createColor(IColor.GeneraColor.WHITE));
            tech.drawLine(dxx, dy+2, dxx+1, dy+fontValue.get().getFontHeight());
            tech.setColor(oldc);
        }
    }    

    //key type,key up,
    //mouse click,
    @Override
    protected void loadOutFromFace(UIFace face) {
        face.removeMsgUiMap(InputServlet.EventCode_KeyType, this);
        face.removeMsgUiMap(InputServlet.EventCode_KeyUp, this);
        face.removeMsgUiMap(InputServlet.EventCode_MouseClick, this);
    }

    @Override
    protected void loadInToFace(UIFace face) {
        face.addMsgUiMap(InputServlet.EventCode_KeyType, this);
        face.addMsgUiMap(InputServlet.EventCode_KeyUp, this);
        face.addMsgUiMap(InputServlet.EventCode_MouseClick, this);
    }

    //key type,key up
    //mouse click
    @Override
    public boolean handle(Message msg) {
        long type = msg.msgType;
        if (type == InputServlet.EventCode_KeyType) {
            if (isOn) {
                char word = (char) msg.extraObjs[0];
                int code = (word);
                DebugUtil.log_panel(new StringProperty("type - code:" + code));
                if (code != InputServlet.KeyCode_BackSpace&&code!=InputServlet.KeyCode_Esc&&code!=InputServlet.KeyCode_Enter) {
                    append(word);
                }
                return true;
            }
        } else if (type == InputServlet.EventCode_KeyUp) {
            if (isOn) {
                int code = (int) msg.extraObjs[0];
                DebugUtil.log_panel(new StringProperty("up - code:" + code));
                if (code == InputServlet.KeyCode_BackSpace) {
                    back();
                    return true;
                }
            }
        } else if (type == InputServlet.EventCode_MouseClick) {
            int mx = (int) msg.extraObjs[0];
            int my = (int) msg.extraObjs[1];
            boolean isIn = isPointInUi(mx, my);
            if (isOn && !isIn) {
                isOn = false;
                return false;//交给其他UI处理
            } else if (!isOn && isIn) {
                isOn = true;
                return false;//交给其他UI处理
            }
            return false;
        }
        return false;
    }

    public static class UIInputLineCreator implements IUICreator {

        /*
            <UIInputLine name pos secret="">
                <back path=""/> 可无
                <color></color> 可无
                <font></font> 可无
                <size></size> 可无
                <length></length> 可无
                <holder></holder> 可无
            </UIInputLine>
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

            boolean bsecret = false;
            ITexture back = null;
            int length = 10;
            String holder = "Input";
            String esecret = element.attributeValue("secret");
            if (esecret != null && esecret.equals("true")) {
                bsecret = true;
            }
            Element eback = element.element("back");
            if (eback != null) {
                ITexture backt = createTexture(eback);
                if (backt != null) {
                    back = backt;
                }
            }
            Element elength = element.element("length");
            if (elength != null) {
                int ilength = Integer.parseInt(elength.getText());
                length = ilength;
            }
            Element eholder = element.element("holder");
            if (eholder != null) {
                holder = eholder.getText();
            }

            IFont font;
            IColor color = null;
            Element fonte = element.element("font");
            String sizes = element.elementText("size");
            if(sizes==null)sizes="20";

            String path = null;
            if (fonte == null) {
                font = ((IGraphicsTech) ((WindowApp) App.getInstance())
                        .getGraphicsTech()).createFont("Dialog",
                        IFont.FontStyle.PLAIN, Integer.parseInt(sizes));
            } else if (fonte.attribute("path") != null) {
                path = fonte.attributeValue("path");
                font = ((IGraphicsTech) ((WindowApp) App.getInstance()).
                        getGraphicsTech()).createFontByPath(fonte.getText(),
                        IFont.FontStyle.PLAIN, Integer.parseInt(sizes));
            } else {
                font = ((IGraphicsTech) ((WindowApp) App.getInstance())
                        .getGraphicsTech()).createFont("Dialog",
                        IFont.FontStyle.PLAIN, Integer.parseInt(sizes));
            }
            Element colore = element.element("color");
            String colors;

            if (colore == null) {
                colors = IColor.GeneraColor.ORANGE.toString();
            } else {
                colors = element.elementText("color");
            }
            if(colors.contains(",")){
                String[] coloris=colors.split(",");
                if(coloris.length!=3){
                    color=((IGraphicsTech) ((WindowApp) App.getInstance()).
                            getGraphicsTech()).createColor(IColor.GeneraColor.WHITE);
                }
                else{
                    color=((IGraphicsTech) ((WindowApp) App.getInstance()).
                            getGraphicsTech()).createColor(Integer.parseInt(coloris[0]),
                            Integer.parseInt(coloris[1]),Integer.parseInt(coloris[2]),Integer.parseInt(coloris[3]));
                }
            }else{
                color = ((IGraphicsTech) ((WindowApp) App.getInstance()).
                        getGraphicsTech()).createColor(IColor.GeneraColor.valueOf(colors));
            }

            color = tech.createColor(IColor.GeneraColor.valueOf(colors));

            UIInputLine line = new UIInputLine(name, pos, length, color, font);
            line.setPlaceHolder(new StringProperty(holder));
            if(back!=null){
                line.setBack(back);
            }
            line.setBack(back);
            line.enableSecretMode(bsecret);

            return line;
        }

        private final IGraphicsTech tech = ((WindowApp) App.getInstance()).getGraphicsTech();

        private Vector createPos(Element element) {
            String poss = element.attributeValue("pos");
            String[] posl = poss.split(",");
            double x = Double.parseDouble(posl[0]);
            double y = Double.parseDouble(posl[1]);
            return new Vector(x, y, 0, 0);
        }

        private ITexture createTexture(Element element) {
            String path = element.attributeValue("path");
            if (path == null) {
                DebugUtil.log("path for texture is not defined.return null texture");
                return null;
            } else {
                return tech.createTexture(path);
            }
        }
    }
}
