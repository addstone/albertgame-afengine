/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.graphics.IGraphicsTech;
/**
 *
 * @author Albert Flex
 */
public class AppTest {
    
    public static void main(String[] args) {
        new AppTest().launchWindowTest();
    }
    
    public void launchWindowTest(){
        
//        URL url=getClass().getClassLoader().getResource("duke0.gif");
//        String title="Title1";
//        
        WindowApp win=new WindowApp(null,"title","src/main/java/resources/duke0.gif");
        IGraphicsTech tech=win.getGraphicsTech();
        tech.setRootDrawStrategy((tec)->{
            tec.drawText(0,0, tech.getFont(), tech.getColor(),"FPS:"+tech.getFPS());
        });
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
