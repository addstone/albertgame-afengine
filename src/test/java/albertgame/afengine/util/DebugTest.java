/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import org.testng.annotations.Test;

/**
 *
 * @author Albert Flex
 */
public class DebugTest {
    
    @Test
    public void testLog(){
        DebugUtil util=new DebugUtil();
        util.switchOn();
        System.out.println("\033[30m" + "就是酱紫的");
        util.log(DebugUtil.LogType.INFO,"test Log");
        util.log(DebugUtil.LogType.WARNING,"test warning");
        util.log(DebugUtil.LogType.ERROR,"test error");    
    }
    
    @Test
    public void testAssert(){
        DebugUtil util=new DebugUtil();
        util.switchOn();
        
        int a=1,b=2;
        util.assertEqual(a==b, DebugUtil.LogType.ERROR,"1 should equal to 2");
        
        String text=null;        
        util.assertNotNull(text, DebugUtil.LogType.ERROR, "text must not null!");
    }
}
