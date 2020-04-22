/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import albertgame.afengine.util.property.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.Test;


public class PropertyTest {

    private static final Logger log = Logger.getLogger(PropertyTest.class.getName());
    
    @Test
    public void testLongValue(){
        LongProperty value=new LongProperty(1000L);
        log.log(Level.INFO, "long value before change:{0}", value.get());

        value.getValueListeners().add((AbValueProperty.IChange<Long>) (Long oldValue, Long newValue) -> {
            log.log(Level.INFO, "change! from {0} to {1}", new Object[]{oldValue, newValue});
        });
        
        value.set(3000L);
        log.log(Level.INFO, "long value after change:{0}", value.get());
    }
    
    @Test
    public void testIntValue(){
        IntProperty value=new IntProperty(200);
        log.log(Level.INFO, "int value before change:{0}", value.get());

        value.getValueListeners().add((AbValueProperty.IChange<Integer>) (Integer oldValue, Integer newValue) -> {
            log.log(Level.INFO, "change! from {0} to {1}", new Object[]{oldValue, newValue});
        });
        
        value.set(700);
        log.log(Level.INFO, "int value after change:{0}", value.get());
    }
    
    @Test
    public void testDoubleValue(){
        DoubleProperty value=new DoubleProperty(200.0);
        log.log(Level.INFO, "double value before change:{0}", value.get());

        value.getValueListeners().add((Double oldValue, Double newValue) -> {
            log.log(Level.INFO, "change! from {0} to {1}", new Object[]{oldValue, newValue});
        });
        
        value.set(70.0);
        log.log(Level.INFO, "double value after change:{0}", value.get());        
    }
    
    @Test
    public void testStringValue(){
        TextUtil.get().addText("albert","阿尔伯特");
        TextUtil.get().addText("flex","弗莱克斯");
        StringProperty value=new StringProperty("@albert");
        log.log(Level.INFO, "string value before change:{0}", value.get());

        value.getValueListeners().add((String oldValue, String newValue) -> {
            log.log(Level.INFO, "change! from {0} to {1}", new Object[]{oldValue, newValue});
        });
        
        value.set("@flex");
        log.log(Level.INFO, "string value after change:{0}", value.get());                
    }
    
    @Test
    public void testValueList(){
        ValuePropertyList<String> values=new ValuePropertyList<>();
        values.getChangelistener().add((List<String> valuelist, String newvalue, int newvalueIndex) -> {
            log.log(Level.INFO, "list add value {0},at index of:{1}", new Object[]{newvalue, newvalueIndex});
        });
        
        values.addValueProperty("albert-flex");
        values.addValueProperty("shalock-honers");
    }
    
    @Test
    public void testValueBind(){
        StringProperty value1=new StringProperty("albert");
        StringProperty value2=new StringProperty("flex");
        AbValuePropertyBind<String> bind=new AbValuePropertyBind(value1,value2){
            @Override
            public String calcValue(Object oldvalue, AbValueProperty[] bindproperty) {
                String res="";
                for(AbValueProperty<String> value:bindproperty){
                    res+="-"+value.get();
                }
                return res;
            }
        };
        
        log.log(Level.INFO, "bind value before change is : {0}", bind.get());
        
        value1.set("shalock");
        value2.set("honers");
        
        log.log(Level.INFO, "bind value after change is : {0}", bind.get());
    }
}
