/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import org.testng.annotations.Test;

/**
 *
 * @author Albert Flex
 */
public class AppTest {
    
    public static void main(String[] args) {
        logicTest();
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
