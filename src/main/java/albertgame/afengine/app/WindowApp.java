/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.graphics.GraphicsTech_Java2DImpl2;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import albertgame.afengine.input.InputAdapter;
import albertgame.afengine.app.AppBoot.IXMLAppTypeBoot;
import albertgame.afengine.graphics.IDrawStrategy;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import java.util.Iterator;
import org.dom4j.Element;

public class WindowApp extends App {

    public static final String APPTYPE = "Window";

    private final IGraphicsTech graphicsTech;
    int x, y, width, height;
    boolean full;
    String title;
    ITexture icon;
    boolean center;

    public WindowApp(IAppLogic logic, String title, String iconpath) {
        super(WindowApp.APPTYPE, title, logic);
        IGraphicsTech tech = new GraphicsTech_Java2DImpl2();
        graphicsTech = tech;
        icon = graphicsTech.createTexture(iconpath);
        this.title = title;
        full = true;
        width = tech.getMoniterWidth();
        height = tech.getMoniterHeight();
    }

    public WindowApp(IAppLogic logic, String title, String iconpath, int width, int height) {
        super(WindowApp.APPTYPE, title, logic);
        IGraphicsTech tech = new GraphicsTech_Java2DImpl2();
        graphicsTech = tech;
        icon = graphicsTech.createTexture(iconpath);
        this.title = title;
        full = false;
        center = true;
        if (width > tech.getMoniterWidth()) {
            this.width = tech.getMoniterWidth();
        } else {
            this.width = width;
        }
        if (height > tech.getMoniterHeight()) {
            this.height = tech.getMoniterHeight();
        } else {
            this.height = height;
        }
    }

    public WindowApp(String name, IAppLogic logic,
            int x, int y, int width, int height, String title,
            ITexture icon, IGraphicsTech tech) {
        super(WindowApp.APPTYPE, name, logic);
        this.x = x;
        this.y = y;
        center = false;
        if (width > tech.getMoniterWidth()) {
            this.width = tech.getMoniterWidth();
        } else {
            this.width = width;
        }
        if (height > tech.getMoniterHeight()) {
            this.height = tech.getMoniterHeight();
        } else {
            this.height = height;
        }
        full = false;
        this.icon = icon;
        this.title = title;
        this.graphicsTech = tech;
    }

    public WindowApp(String name, IAppLogic logic,
            int width, int height, String title, ITexture icon, IGraphicsTech tech) {
        super(WindowApp.APPTYPE, name, logic);
        center = true;
        if (width > tech.getMoniterWidth()) {
            this.width = tech.getMoniterWidth();
        } else {
            this.width = width;
        }
        if (height > tech.getMoniterHeight()) {
            this.height = tech.getMoniterHeight();
        } else {
            this.height = height;
        }
        full = false;
        this.icon = icon;
        this.title = title;
        this.graphicsTech = tech;
    }

    public WindowApp(String name, IAppLogic logic,
            String title, ITexture icon, IGraphicsTech tech) {
        super(WindowApp.APPTYPE, name, logic);
        full = true;
        this.icon = icon;
        this.title = title;
        this.graphicsTech = tech;
    }

    @Override
    public boolean shutdownApp() {
        graphicsTech.destroy();
        return true;
    }

    @Override
    public boolean updateApp(long time) {
        if (graphicsTech != null) {
            graphicsTech.callDraw();
        }
        return true;
    }

    boolean created = false;

    public void create() {
        if (!created) {
            if (full) {
                graphicsTech.create(icon, title);
            } else {
                if (center) {
                    graphicsTech.create(width, height, icon, title);
                } else {
                    graphicsTech.create(x, y, width, height, icon, title);
                }
            }
            InputAdapter adapter = new InputAdapter();
            graphicsTech.setValue("keylistener", new Object[]{adapter});
            graphicsTech.setValue("mouselistener", new Object[]{adapter});
            graphicsTech.setValue("mousemovelistener", new Object[]{adapter});
            graphicsTech.setValue("mousewheellistener", new Object[]{adapter});
            graphicsTech.setValue("windowlistener", new Object[]{adapter});
            created = true;
        }
    }

    @Override
    public boolean initApp() {
        if (graphicsTech == null) {
            System.out.println("You should set a graphicsTech for WindowApp ,while you haven't done this.");
            return false;
        }
        create();
        return true;
    }

    public IGraphicsTech getGraphicsTech() {
        return graphicsTech;
    }

    public static class WindowAppBoot implements IXMLAppTypeBoot {

        /*
            <window size="full" or size="800,600" tech="" title="" icon="">                
                <before>
                    <draw pri="" class="" />
                </before>
                <root class=""/>
                <after>
                    <draw pri="" class=""/>
                </after>
            </window>
         */
        @Override
        public App bootApp(Element element) {
            WindowApp wapp = null;
            boolean full = false;
            int width = 800;
            int height = 600;
            String sizee = element.attributeValue("size");
            if (sizee != null) {
                if (sizee.equals("full")) {
                    full = true;
                } else {
                    String[] size = sizee.split(",");
                    if (size.length == 2) {
                        width = Integer.parseInt(size[0]);
                        height = Integer.parseInt(size[1]);
                    }
                }
            }
            String title = element.attributeValue("title");
            IGraphicsTech tech = (IGraphicsTech) FactoryUtil.get().create(element.attributeValue("tech"));
            if (tech == null){
                System.out.println("window tech class not found!");
                return null;
            }
            String iconpath = element.attributeValue("icon");
            ITexture icont = tech.createTexture(iconpath);
            if (full) {
                wapp = new WindowApp(title,null,title, icont, tech);
            } else {
                wapp = new WindowApp(title,null,width, height,title,icont, tech);
            }

            /*
                <before>
                    <draw pri="" class="" />
                </before>
                <root class=""/>
                <after>
                    <draw pri="" class=""/>
                </after>                
             */
            Element be = element.element("before");
            if (be != null) {
                Iterator<Element> beiter = be.elementIterator();
                long pril = 1;
                while (beiter.hasNext()) {
                    Element before = beiter.next();
                    String pri = before.attributeValue("pri");
                    if (pri == null) {
                        ++pril;
                    } else {
                        pril = Long.parseLong(pri);
                    }
                    IDrawStrategy strategy = (IDrawStrategy) FactoryUtil.get().create(before.attributeValue("class"));
                    if (strategy == null) {
                        System.out.println("strategy class not found,will skeep for.");
                        continue;
                    }
                    DebugUtil.log("add before draw..");
                    tech.addBeforeDrawStrategy(pril, strategy);
                }
            }
            Element root = element.element("root");
            if (root != null) {
                IDrawStrategy rootstrategy = (IDrawStrategy) FactoryUtil.get().create(root.attributeValue("class"));
                if (rootstrategy == null) {
                    System.out.println("root strategy class not found,will skeep for." + root.attributeValue("class"));
                } else {
                    tech.setRootDrawStrategy(rootstrategy);
                }
            }
            Element af = element.element("after");
            if (af != null) {
                Iterator<Element> afiter = af.elementIterator();
                long pril2 = 1;
                while (afiter.hasNext()) {
                    Element after = afiter.next();
                    String pri = after.attributeValue("pri");
                    if (pri == null) {
                        ++pril2;
                    } else {
                        pril2 = Long.parseLong(pri);
                    }
                    IDrawStrategy strategy = (IDrawStrategy) FactoryUtil.get().create(after.attributeValue("class"));
                    if (strategy == null) {
                        System.out.println("strategy class not found,will skeep for." + after.attributeValue("class"));
                        continue;
                    }
                    DebugUtil.log("add after draw..");
                    tech.addAfterDrawStrategy(pril2, strategy);
                }
            }

            return wapp;
        }
    }
}
