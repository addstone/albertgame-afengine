package albertgame.afengine.util;

import java.util.HashMap;
import java.util.Map;


public class FactoryUtil {
    
    static FactoryUtil util;
    public static FactoryUtil get(){
        if(util==null)
            util=new FactoryUtil();
        return util;
    }
    
    public static interface IFactory{
       Object create(Object ... args);
    }
    
    private final Map<String,IFactory> factoryMap;
    private final String pac=".";

    private FactoryUtil() {
        this.factoryMap =new HashMap<>();
    }
    public Map<String, IFactory> getFactoryMap() {
        return factoryMap;
    }
    public void putFactory(String type,String name,IFactory factory){
        factoryMap.put(type+pac+name, factory);
    }
    public IFactory getFactory(String type,String name){
        String realname=type+pac+name;
        return factoryMap.get(realname);
    }
    public String getRealName(String type,String name){
        return type+pac+name;
    }
    public  Object create(String type,String name,Object ... args){
        String realname=type+pac+name;
        IFactory factory=factoryMap.get(realname);
        if(factory==null)return null;
        
        Object obj=factory.create(args);
        return obj;
    }
}
