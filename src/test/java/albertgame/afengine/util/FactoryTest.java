/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import albertgame.afengine.util.FactoryUtil.IFactory;
import org.testng.annotations.Test;
import albertgame.afengine.util.math.Vector;
import org.testng.Assert;
/**
 *
 * @author Albert Flex
 */
public class FactoryTest {
    
    @Test
    public void testFactory(){
        FactoryUtil util=new FactoryUtil();
        IFactory fac=(args)->{
            return "Hello";
        };
        IFactory fac2=(args)->{
            return new Vector();
        };
        
        util.putFactory("test","hello", fac);
        util.putFactory("test","hello2", fac2);
        
        String text=(String) util.create("test", "hello",null);
        Assert.assertEquals(text, "Hello", "text failed");
        
        Vector v=(Vector) util.create("test","hello2",null);
        Assert.assertEquals(v,new Vector(), "vector failed");
    }
}
