package albertgame.afengine.util;

import java.util.HashMap;
import java.util.Map;

public class FactoryUtil {

    static FactoryUtil util;

    public static FactoryUtil get() {
        if (util == null) {
            util = new FactoryUtil();
        }
        return util;
    }

    public static interface IFactory {

        Object create(Object... args);
    }

    private final Map<String, IFactory> factoryMap;
    private final String pac = ".";

    private FactoryUtil() {
        this.factoryMap = new HashMap<>();
    }

    public Map<String, IFactory> getFactoryMap() {
        return factoryMap;
    }

    public void putFactory(String type, String name, IFactory factory) {
        factoryMap.put(type + pac + name, factory);
    }

    public IFactory getFactory(String type, String name) {
        String realname = type + pac + name;
        return factoryMap.get(realname);
    }

    public String getRealName(String type, String name) {
        return type + pac + name;
    }

    public Object create(String type, String name, Object... args) {
        String realname = type + pac + name;
        IFactory factory = factoryMap.get(realname);
        if (factory == null) {
            return null;
        }

        Object obj = factory.create(args);
        return obj;
    }

    //albertgame.afengine.app.App
    //app,App1
    public Object create(String name, Object... args) {
        if (name.contains(",")) {
            String[] s = name.split(",");
            if (s.length != 2) {
                return null;
            }
            String type = s[0];
            String typename = s[1];
            return create(type, typename, args);
        }
        try {
            Class<?> cls = Class.forName(name);
            Object obj = cls.newInstance();
            return obj;
        } catch (IllegalAccessException | InstantiationException ex) {
            DebugUtil.error("class load error!");
        } catch (ClassNotFoundException ex) {
            DebugUtil.error("class load error:class not found!");
        }
        return null;
    }
    
    public Object create(String name){
        return create(name, (Object[]) null);
    }
}
