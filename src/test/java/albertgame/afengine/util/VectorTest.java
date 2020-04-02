/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import albertgame.afengine.util.math.Vector;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Administrator
 */
public class VectorTest {
    
    @Test
    public void testVec(){
        Vector v1=new Vector(10,10,10,0);
        Vector v2=new Vector(10,20,2,0);
        
        Assert.assertEquals(v1.dotVector(v2),320.0,"dot failed");        

        Vector realresult=new Vector(20,30,12,0);
        Assert.assertEquals(realresult,v1.addVector(v2),"add failed");
        
        double length=Math.sqrt(10*10+10*10+10*10);
        Assert.assertEquals(length,v1.getLength(),"length failed");    
        
        Vector v4=new Vector(100,0,0,0);
        Vector v5=new Vector(10,70,0,0);
        double cos=v4.cosAngleWithVector(v5);
        System.out.println("cos angle:"+Math.toDegrees(Math.acos(cos)));
    }
}
