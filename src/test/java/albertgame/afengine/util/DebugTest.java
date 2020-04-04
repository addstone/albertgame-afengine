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
        DebugUtil.switchOn();
        DebugUtil.log(DebugUtil.LogType.INFO,"test Log");
        DebugUtil.log(DebugUtil.LogType.WARNING,"test warning");
        DebugUtil.log(DebugUtil.LogType.ERROR,"test error");    
    }
    
    @Test
    public void testAssert(){
        DebugUtil.switchOn();
        
        int a=1,b=2;
        DebugUtil.assertEqual(a==b, DebugUtil.LogType.ERROR,"1 should equal to 2");
        
        String text=null;        
        DebugUtil.assertNotNull(text, DebugUtil.LogType.ERROR, "text must not null!");
    }
}
