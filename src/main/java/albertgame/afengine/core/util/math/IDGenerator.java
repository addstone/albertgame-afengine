/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.util.math;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author albert-flex
 */
public class IDGenerator {

    private long tmpID = 74;

    public synchronized long createId() {
        long ltime = 0;
        synchronized(this){
            //当前：（年、月、日、时、分、秒、毫秒）*18954
            ltime = Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(new Date())) * 18954;
            if (tmpID < ltime) {
                tmpID = ltime;
            } else {
                tmpID = tmpID + 1;
                ltime = tmpID;
            }
            return ltime;
        }
    }
}
