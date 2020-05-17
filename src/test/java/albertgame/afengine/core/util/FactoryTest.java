/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.util;

import albertgame.afengine.core.util.FactoryUtil.IFactory;
import albertgame.afengine.core.util.math.Vector;
import org.testng.annotations.Test;
import org.testng.Assert;
/**
 *
 * @author Albert Flex
 */
public class FactoryTest {
    
    @Test
    public void testFactory(){
        IFactory fac=(args)->{
            return "Hello";
        };
        IFactory fac2=(args)->{
            return new Vector();
        };
        
        FactoryUtil.putFactory("test","hello", fac);
        FactoryUtil.putFactory("test","hello2", fac2);
        
        String text=(String) FactoryUtil.create("test", "hello",null);
        Assert.assertEquals(text, "Hello", "text failed");
        
        Vector v=(Vector) FactoryUtil.create("test","hello2",null);
        Assert.assertEquals(v,new Vector(), "vector failed");
    }
    
    public static class Class1{
        String name;
        boolean pass;

        public Class1(String name,boolean pass) {
            this.name=name;
            this.pass=pass;
        }

        public Class1() {
            this("class from reflect!",false);
        }
        

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isPass() {
            return pass;
        }

        public void setPass(boolean pass) {
            this.pass = pass;
        }

        @Override
        public String toString() {
            return "Class1{" + "name=" + name + ", pass=" + pass + '}';
        }
        
    }
    
    public static class CreateClass implements IFactory{
        @Override
        public Object create(Object... args) {
            return new Class1("class1 from create",true);
        }
    }
    
    @Test
    public void testCreate(){
        FactoryUtil.putFactory("boot","boot1",new CreateClass());
        Class1 clss1=(Class1)FactoryUtil.create("albertgame.afengine.core.util.FactoryTest$Class1");
        Class1 clss2=(Class1)FactoryUtil.create("boot,boot1");
        System.out.println(clss1);
        System.out.println(clss2);
    }
}
