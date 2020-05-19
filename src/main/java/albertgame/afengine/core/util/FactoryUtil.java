package albertgame.afengine.core.util;

import albertgame.afengine.core.app.WindowApp;
import albertgame.afengine.core.input.InputServlet;
import albertgame.afengine.core.message.XMLMessagePartBoot;
import albertgame.afengine.in.components.action.ActionComponent;
import albertgame.afengine.in.components.action.ActionComponentFactory;
import albertgame.afengine.in.components.behavior.BehaviorBeanComponent;
import albertgame.afengine.in.components.behavior.BehaviorBeanComponentFactory;
import albertgame.afengine.in.components.render.RenderComponentFactory;
import albertgame.afengine.in.components.render.SceneRenderComponentDraw;
import albertgame.afengine.in.core.graphics.GraphicsTech_Java2DImpl;
import albertgame.afengine.in.core.graphics.GraphicsTech_Java2DImpl2;
import albertgame.afengine.in.parts.input.InputRoute;
import albertgame.afengine.in.parts.input.XMLInputPartBoot;
import albertgame.afengine.in.parts.scene.ActorComponent;
import albertgame.afengine.in.parts.scene.XMLScenePartBoot;
import java.util.HashMap;
import java.util.Map;

public class FactoryUtil {

    private static Map<String, String> oldObj;
//afengine.part.message.MessageHandlerRoute

    public static void initEngineFactory(){
        
        //--type-boot--
        putFactory("type-boot","win",(args)->new WindowApp.WindowAppBoot());
        
        //--graphicstech---
        //add graphicstech:graphicstech,java2dimpl 
        //add graphicstech:graphicstech,java2dimp2 
        putFactory("graphicstech","java2dimpl",(args)->new GraphicsTech_Java2DImpl());
        putFactory("graphicstech","java2dimpl2",(args)->new GraphicsTech_Java2DImpl2());
        
        //--draw--
        //add debugdrawstrategy:drawstrategy,debug
        //add scenedrawstrategy:drawstrategy,scene        
        putFactory("drawstrategy","debug",(args)->new DebugUtil.DebugDrawStrategy());
        putFactory("drawstrategy","scene",(args)->new SceneRenderComponentDraw());
        
        
        //--xmlpart--
        //add xmlmessagepart:xmlpart,msg        
        putFactory("xmlpart","msg",(args)->new XMLMessagePartBoot());
        putFactory("xmlpart","input",(args)->new XMLInputPartBoot());
        putFactory("xmlpart","scene",(args)->new XMLScenePartBoot());
        
        
        //--component-factory--
        putFactory("comp-fac","render",(args)->new RenderComponentFactory());
        putFactory("comp-fac","behaviorbean",(args)->new BehaviorBeanComponentFactory());
        putFactory("comp-fac","action",(args)->new ActionComponentFactory());
        
        
        //add route:msg-route,input
        //add exit-handler:input,exit
        putFactory("msg-route","input",(args)->new InputRoute());
        putFactory("input","exit",(args)->new InputServlet.ExitHandler());        
        
    }
    
    static {
        oldObj = new HashMap<>();
        oldObj.put("afengine.component.render.GraphicsTech_Java2D",
                "albertgame.in.core.graphics.GraphicsTech_Java2DImpl");
        oldObj.put("afengine.core.util.Debug$DebugDrawStrategy",
                "albertgame.afengine.core.util.DebugUtil$DebugDrawStrategy");
        oldObj.put("afengine.core.WindowApp$WindowAppBoot",
                "albertgame.afengine.core.app.WindowApp$WindowAppBoot");
        oldObj.put("afengine.part.message.XMLMessagePartBoot",
                "albertgame.afengine.core.message.XMLMessagePartBoot");
        oldObj.put("afengine.part.message.MessageHandlerRoute",
                "albertgame.afengine.core.message.MessageHandlerRoute");
        oldObj.put("afengine.component.render.SceneRenderComponentDraw",
                "albertgame.afengine.in.components.render.SceneRenderComponentDraw");
    }

    public static interface IFactory {

        Object create(Object... args);
    }

    private static final Map<String, IFactory> factoryMap = new HashMap<>();

    private static final String pac = ",";

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
    //app,App1 默认不放入已创建的对象Map [since 1.0版本]
    //app,App1,only  [since 1.0版本]默认放入已创建的对象map,key为app,App1
    public static Object create(String name, Object... args) {
        if (name.contains(",")) {
            String[] s = name.split(",");
            if (s.length == 3) {
                if (s[2].equals("only")) {
                    if (createdObjMap.containsKey(s[0]+","+s[1])) {
                        return createdObjMap.get(s[0]+","+s[1]);
                    } else {
                        String type = s[0];
                        String typename = s[1];
                        Object obj = create(type, typename, args);
                        createdObjMap.put(s[0]+","+s[1], obj);
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
