/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.graphics.GraphicsTech_Java2DImpl;
import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;

/**
 *
 * @author Albert Flex
 */
public class WindowApp extends App {

    public static final String APPTYPE = "Window";

    private final IGraphicsTech graphicsTech;
    int x, y, width, height;
    boolean full;
    String title;
    ITexture icon;
    boolean center;
    
    public WindowApp(IAppLogic logic,String title,String iconpath){
        super(WindowApp.APPTYPE,title,logic);
        IGraphicsTech tech=new GraphicsTech_Java2DImpl();
        graphicsTech=tech;
        icon=graphicsTech.createTexture(iconpath);
        this.title=title;
        full=true;
        width=tech.getMoniterWidth();
        height=tech.getMoniterHeight();
    }
    
    public WindowApp(IAppLogic logic,String title,String iconpath,int width,int height){
        super(WindowApp.APPTYPE,title,logic);
        IGraphicsTech tech=new GraphicsTech_Java2DImpl();
        graphicsTech=tech;
        icon=graphicsTech.createTexture(iconpath);
        this.title=title;
        full=false;
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
            created=true;
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
}
