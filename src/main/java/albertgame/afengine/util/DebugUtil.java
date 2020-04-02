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

    private boolean on = false;

    private final Map<String, String> colorMap;

    public DebugUtil() {
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

    public void switchOn() {
        on = true;
    }

    public void switchOff() {
        on = false;
    }

    public void log(LogType logType, String info) {
        if (!on) {
            return;
        }

        Date date = new Date();

        String outinfo = "\033[" + colorMap.get(logType.toString()) + "m" + logType.toString() +"\t"+ ">" + date.toString() + ">" + info+"\033[m";
        System.out.println(outinfo);
        if (logType == LogType.SEVER) {
            System.exit(0);
        }
    }

    public boolean assertEqual(boolean statement, LogType logType, String info) {
        if (!on) {
            System.out.println("you need open debug on to use assert");
            return false;
        }

        if (!statement) {
            Date date = new Date();
            System.out.println("assert:" + logType.toString() + ">" + date.toString() + ">" + info);
            if (logType == LogType.SEVER) {
                System.exit(0);
            }
            return true;
        }
        return false;
    }

    public boolean assertNotNull(Object obj, LogType logType, String info) {
        return assertEqual(obj != null, logType, info);
    }
}
