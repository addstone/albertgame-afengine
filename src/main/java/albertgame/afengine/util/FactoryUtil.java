package albertgame.afengine.util;

import java.util.HashMap;
import java.util.Map;


public class FactoryUtil {
    
    public static interface IFactory{
       Object create(Object ... args);
    }
    
    private final Map<String,IFactory> factoryMap;

    public FactoryUtil() {
        this.factoryMap =new HashMap<>();
    }

    public Map<String, IFactory> getFactoryMap() {
        return factoryMap;
    }
}
