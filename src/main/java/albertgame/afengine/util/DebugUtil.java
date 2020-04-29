/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import albertgame.afengine.graphics.*;
import albertgame.afengine.util.property.StringProperty;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Albert Flex
 */
public class DebugUtil {

    private static boolean on = false;

    private static final Map<String, String> colorMap;

    static{
        colorMap = new HashMap<>();
        colorMap.put(LogType.INFO.toString(), "36");//green-blue
        colorMap.put(LogType.ERROR.toString(), "35");//purple-red
        colorMap.put(LogType.WARNING.toString(), "33");//yellow
        colorMap.put(LogType.SEVER.toString(), "31");//red
    }

    public static enum LogType {
        INFO,
        WARNING,
        ERROR,
        SEVER
    }

    public static void  switchOn() {
        on = true;
    }

    public static void  switchOff() {
        on = false;
    }
    
    public static void log(String info){
        log(LogType.INFO,info);
    }
    
    public static void error(String info){
        log(LogType.ERROR,info);        
    }
    
    public static void  log(LogType logType, String info) {
        if (!on) {
            return;
        }
        
        Date date = new Date();
;       String outinfo = "\033["+colorMap.get(logType.toString()) + "m"+ info+"\033[m";
        System.out.println(outinfo);
        if (logType == LogType.SEVER) {
            System.exit(0);
        }
    }

    public static boolean  assertEqual(boolean statement, LogType logType, String info) {
        if (!on) {
            System.out.println("you need open debug on to use assert");
            return false;
        }

        if (!statement) {
            log(logType,info);
            return true;
        }
        return false;
    }

    public static boolean  assertNotNull(Object obj, LogType logType, String info) {
        return assertEqual(obj != null, logType, info);
    }

    public static final Deque<StringProperty> logTexts = new ArrayDeque<>();

    public static void log_panel(StringProperty text) {
        if(!on)return;

        logTexts.addLast(text);
        if(logTexts.size()>30){
            logTexts.pollFirst();
        }
    }    
    public static void clear_panellog(){
        logTexts.clear();
    }
    
    /*
        if you want look Debug.log_panel ,please add a instance of DebugDrawStrategy to RenderStrategy.after
    */
    public static class DebugDrawStrategy implements IDrawStrategy{
        IColor color;
        @Override
        public void draw(IGraphicsTech tech) {
            if(color==null){
                color=tech.createColor(IColor.GeneraColor.RED);
            }

            Iterator<StringProperty> logiter = logTexts.iterator();
            int height = 0;
            try{
              while(logiter.hasNext()){
                StringProperty tex = logiter.next();
                if(tex!=null){
                    tech.drawText(0, height,tech.getFont(),color,tex.get());
                    height+=tech.getFont().getFontHeight();                    
                }
              }
            }catch(Exception ex){
                DebugUtil.log("debug log draw failed once.");
            }
            
            tech.drawText(0,0,tech.getFont(), color,
                    "FPS:"+tech.getFPS());
        }        
    }

}
