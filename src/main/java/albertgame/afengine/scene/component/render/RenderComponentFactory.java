package albertgame.afengine.scene.component.render;

import albertgame.afengine.app.App;
import albertgame.afengine.app.WindowApp;
import albertgame.afengine.graphics.IColor;
import albertgame.afengine.graphics.IFont;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import albertgame.afengine.scene.Actor;
import albertgame.afengine.scene.component.ActorComponent;
import albertgame.afengine.scene.component.IComponentFactory;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.TextUtil;
import albertgame.afengine.util.property.StringProperty;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Element;

/**
 * factory for rendercomp,default contains StringPropertyRender and StringPropertyureRender<br>
 *
 * @see TextRenderComponent
 * @see StringPropertyureRenderComponent
 * @author Albert Flex
 */
public class RenderComponentFactory implements IComponentFactory {

    public static interface IRenderCreator {

        public String getType();

        public RenderComponent create(Element element, Map<String, String> datas);
    }

    public static final Map<String, IRenderCreator> extraCreatorMap = new HashMap<>();

    /*
        <Render type="" render-creator="" order="0" useroderclass=""/>
        </Render>
     */
    @Override
    public ActorComponent createComponent(Element element, Map<String, String> datas) {
        RenderComponent comp;
        switch (element.attributeValue("type")) {
            case "StringProperty":
                comp = createStringProperty(element, datas);
                break;
            case "StringPropertyure":
                comp = createTexture(element, datas);
                break;
            case "Rect":
                comp = createRect(element, datas);
                break;
            default:
                comp = createExtra(element, datas);
                break;
        }
        if (comp != null) {
            String order = element.attributeValue("order");
            if (order != null) {
                int orderi = Integer.parseInt(TextUtil.getRealValue(order, datas));
                comp.setRenderOrder(orderi);
            }
        }
        String userorderclass = element.attributeValue("userorderclass");
        if (userorderclass != null) {
            userorderclass = TextUtil.getRealValue(userorderclass, datas);
            Comparator<Actor> comparator = (Comparator) FactoryUtil.get().create(userorderclass);
            if (comparator != null) {
                RenderComponent.setComparator(comparator);
            }
        }
        return comp;
    }

    /*
        <Render type="" render-creator="" order="0"/>
        </Render>
     */
    private RenderComponent createExtra(Element element, Map<String, String> datas) {
        String type = element.attributeValue("type");
        IRenderCreator rc = extraCreatorMap.get(type);
        if (rc == null) {
            String renderclass = element.attributeValue("render-creator");
            if (renderclass != null) {
                rc = (IRenderCreator) FactoryUtil.get().create(renderclass);
                extraCreatorMap.put(type, rc);
            } else {
                DebugUtil.log("have no render-creator for this type");
                return null;
            }
        }
        return rc.create(element, datas);
    }

    /**
     * <Render type="StringProperty">
     * <text></text>
     * <font path=""></font>
     * <size></size>
     * <color rgba=""></color>
     *      //<backcolor></backcolor>
     * </Render>
     */
    private RenderComponent createStringProperty(Element element, Map<String, String> datas) {
        StringProperty text;
        IFont font;
        IColor color = null;
        IColor backcolor = null;
        Element texte = element.element("text");
        if (texte != null) {
            text = new StringProperty(TextUtil.getRealValue(texte.getText(), datas));
        } else {
            text = new StringProperty("DefaultStringProperty");
        }
        Element fonte = element.element("font");
        String sizes = element.elementText("size");
        int siz = Integer.parseInt(TextUtil.getRealValue(sizes, datas));
        String path = null;
        if (fonte == null) {
            font = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createFont("Dialog",
                            IFont.FontStyle.PLAIN, siz);
        } else if (fonte.attribute("path") != null) {
            path = TextUtil.getRealValue(fonte.attributeValue("path"), datas);
            font = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createFontByPath(path,IFont.FontStyle.PLAIN, siz);
        } else {
            font = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createFont("Dialog",
                            IFont.FontStyle.PLAIN, siz);
        }
        Element colore = element.element("color");
        String colors;

        //<color rgba="10,10,10,10"/>
        if (null == colore) {
            colors = IColor.GeneraColor.ORANGE.toString();
            color = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createColor(IColor.GeneraColor.valueOf(colors));
        } else {
            String colorv = colore.attributeValue("rgba");
            if (colorv != null) {
                colorv = TextUtil.getRealValue(colorv, datas);
                color = getColor(colorv);
            } else {
                colors = TextUtil.getRealValue(element.elementText("color"), datas);

                color = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createColor(IColor.GeneraColor.valueOf(colors));

            }
        }

        Element backcolore = element.element("backcolor");
        if (backcolore != null) {
            String backcolorv = backcolore.attributeValue("rgba");
            if (backcolorv != null) {
                String backcolorvalue = TextUtil.getRealValue(backcolorv, datas);
                backcolor = getColor(backcolorvalue);
            } else {
                backcolor = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createColor(IColor.GeneraColor.valueOf(TextUtil.getRealValue(backcolore.getText(), datas)));
            }
        }
        TextRenderComponent textcomp;
        if (backcolore != null) {
            textcomp = new TextRenderComponent(font, color, backcolor, text);
        } else {
            textcomp = new TextRenderComponent(font, color, text);
        }

        return textcomp;
    }

    private IColor getColor(String colorvalue) {
        String[] rgba = colorvalue.split(",");
        IColor color = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createColor(
                        Integer.parseInt(rgba[0]),
                        Integer.parseInt(rgba[1]),
                        Integer.parseInt(rgba[2]),
                        Integer.parseInt(rgba[3]));

        return color;
    }

    /**
     * <Render type="Rect">
     * <shape width="" height="" arcwidth="" archeight="" fill=""/>
     * <color value="r,g,b,a"/>
     * </Render>
     */
    private RenderComponent createRect(Element element, Map<String, String> datas) {
        Element shape = element.element("shape");
        Element color = element.element("color");
        String width = TextUtil.getRealValue(shape.attributeValue("width"), datas);
        String height = TextUtil.getRealValue(shape.attributeValue("height"), datas);
        String arcwidth = TextUtil.getRealValue(shape.attributeValue("arcwidth"), datas);
        String archeight = TextUtil.getRealValue(shape.attributeValue("archeight"), datas);
        String colors = TextUtil.getRealValue(color.attributeValue("value"), datas);
        String fill = TextUtil.getRealValue(shape.attributeValue("fill"), datas);

        String[] colorm = colors.split(",");
        String r = colorm[0];
        String g = colorm[1];
        String b = colorm[2];
        String a = colorm[3];
        RectRenderComponent rect = new RectRenderComponent();
        IGraphicsTech tech = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech());

        rect.setColor(tech.createColor(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b), Integer.parseInt(a)));
        rect.setWidth(Integer.parseInt(width));
        rect.setHeight(Integer.parseInt(height));
        rect.setArcWidth(Integer.parseInt(arcwidth));
        rect.setArcHeight(Integer.parseInt(archeight));

        boolean f = Boolean.parseBoolean(fill);
        rect.setFill(f);
        return rect;
    }

    /**
     * <Render type="StringPropertyure">
     * <texture>path</texture>
     *      //<cutsize x="" y="" width="" height=""/>
     * </Render>
     */
    private RenderComponent createTexture(Element element, Map<String, String> datas) {
        String texturepath = TextUtil.getRealValue(element.elementText("texture"), datas);
        DebugUtil.log("texturepath:" + texturepath);
        ITexture texture = ((IGraphicsTech) ((WindowApp) App.getInstance())
                    .getGraphicsTech()).createTexture(texturepath);
        Element cut = element.element("cutsize");
        if (cut != null) {
            String x = cut.attributeValue("x");
            String y = cut.attributeValue("y");
            String width = cut.attributeValue("width");
            String height = cut.attributeValue("height");
            if (x != null && y != null && width != null && height != null) {
                texture = texture.cutInstance(Integer.parseInt(TextUtil.getRealValue(x, datas)),
                        Integer.parseInt(TextUtil.getRealValue(height, datas)),
                        Integer.parseInt(TextUtil.getRealValue(width, datas)),
                        Integer.parseInt(TextUtil.getRealValue(height, datas)));
            }
        }
        return new TextureRenderComponent(texture);
    }

}
