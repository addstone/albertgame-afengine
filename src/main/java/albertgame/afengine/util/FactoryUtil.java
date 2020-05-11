package albertgame.afengine.util;

import java.util.HashMap;
import java.util.Map;

public class FactoryUtil {

    private static Map<String, String> oldObj;
//afengine.part.message.MessageHandlerRoute

    static {
        oldObj = new HashMap<>();
        oldObj.put("afengine.component.render.GraphicsTech_Java2D",
                "albertgame.afengine.graphics.GraphicsTech_Java2DImpl");
        oldObj.put("afengine.core.util.Debug$DebugDrawStrategy",
                "albertgame.afengine.util.DebugUtil$DebugDrawStrategy");
        oldObj.put("afengine.core.WindowApp$WindowAppBoot",
                "albertgame.afengine.app.WindowApp$WindowAppBoot");
        oldObj.put("afengine.part.message.XMLMessagePartBoot",
                "albertgame.afengine.app.message.XMLMessagePartBoot");
        oldObj.put("afengine.part.message.MessageHandlerRoute",
                "albertgame.afengine.app.message.MessageHandlerRoute");
        oldObj.put("afengine.component.render.SceneRenderComponentDraw",
                "albertgame.afengine.scene.component.SceneRenderComponentDraw");
    }

    public static interface IFactory {

        Object create(Object... args);
    }

    private static final Map<String, IFactory> factoryMap = new HashMap<>();

    private static final String pac = ".";

    public static Map<String, IFactory> getFactoryMap() {
        return factoryMap;
    }

    public static void putFactory(String type, String name, IFactory factory) {
        factoryMap.put(type + pac + name, factory);
    }

    public static IFactory getFactory(String type, String name) {
        String realname = type + pac + name;
        return factoryMap.get(realname);
    }

    public static String getRealName(String type, String name) {
        return type + pac + name;
    }

    public static Object create(String type, String name, Object... args) {
        String realname = type + pac + name;
        IFactory factory = factoryMap.get(realname);
        if (factory == null) {
            return null;
        }

        Object obj = factory.create(args);
        DebugUtil.log("create obj L:" + type + "-" + name + ".");
        return obj;
    }

    private static Map<String, Object> createdObjMap = new HashMap<>();

    //albertgame.afengine.app.App 使用反射, [since 0.1版本]
    //app,App1 默认放入已创建的对象Map [since 1.0版本]
    //app,App1,only  [since 1.0版本]
    public static Object create(String name, Object... args) {
        if (name.contains(",")) {
            String[] s = name.split(",");
            if (s.length == 3) {
                if (s[2].equals("only")) {
                    if (createdObjMap.containsKey(name)) {
                        return createdObjMap.get(name);
                    } else {
                        String type = s[0];
                        String typename = s[1];
                        Object obj = create(type, typename, args);
                        createdObjMap.put(name, obj);
                        return obj;
                    }
                } else {
                    DebugUtil.error("Object Parameter :" + s[2] + "Not Support Yet\n now support Are: \'only\'");
                    return null;
                }
            }
            if (s.length == 2) {
                String type = s[0];
                String typename = s[1];
                Object obj = create(type, typename, args);
                if (obj == null) {
                    DebugUtil.error("Object Created Failed, Create Text:[" + name + "]");
                    return null;
                }
                createdObjMap.put(name, obj);
                return obj;
            }
        } else {
            String dest;
            if (oldObj.containsKey(name)) {
                dest = oldObj.get(name);
            } else {
                dest = name;
            }          
            try {
                Class<?> cls = Class.forName(dest);
                Object obj = cls.newInstance();
                return obj;
            } catch (IllegalAccessException | InstantiationException ex) {
                DebugUtil.error("class load error!");
            } catch (ClassNotFoundException ex) {
                DebugUtil.error("class load error:class [" + dest + "] not found!");
            }
        }
        return null;
    }

    public static Object create(String name) {
        return create(name, (Object[]) null);
    }
}
