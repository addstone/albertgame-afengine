/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import java.util.Date;
import java.util.HashMap;
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
        String time=(1900+date.getYear())+"/"+date.getMonth()+"/"+date.getDay()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
;       String outinfo = "\033[" + colorMap.get(logType.toString()) + "m" + logType.toString() +"\t"+ ">" + time + ">" + info+"\033[m";
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
}
