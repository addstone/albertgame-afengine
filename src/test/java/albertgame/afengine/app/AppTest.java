/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.graphics.IGraphicsTech;
import albertgame.afengine.graphics.ITexture;
import albertgame.afengine.graphics.GraphicsTech_Java2DImpl;
import java.net.URL;

/**
 *
 * @author Albert Flex
 */
public class AppTest {
    
    public static void main(String[] args) {
        new AppTest().launchWindowTest();
    }
    
    public void launchWindowTest(){
        
        URL url=getClass().getClassLoader().getResource("duke0.gif");
        IGraphicsTech tech=new GraphicsTech_Java2DImpl();
        ITexture icon=tech.createTexture(url);
        tech.setRootDrawStrategy((tec)->{
            tec.drawText(0,0, tech.getFont(), tech.getColor(),"FPS:"+tech.getFPS());
        });
        String title="Title1";
        
        App win=new WindowApp("win",null,title,icon,tech);
        App.launch(win);
    }
    
    public static void logicTest(){
        IAppLogic logic=new IAppLogic(){
            int i=0;
            @Override
            public boolean init() {
                System.out.println("init logic!!!");
                return true;
            }

            @Override
            public boolean update(long time) {
                ++i;
                if(i>10){
                    App.exit();
                }
                System.out.println("update logic!!!");
                return true;
            }

            @Override
            public boolean shutdown() {
                System.out.println("shutdowm logic!!!");
                return true;
            }
        };
        App app=new App("service","app1",logic);
        App.launch(app);
    }
}
