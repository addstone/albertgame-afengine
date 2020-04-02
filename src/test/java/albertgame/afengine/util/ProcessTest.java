/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import albertgame.afengine.util.process.AbProcess;
import albertgame.afengine.util.process.ProcessManager;
import org.testng.annotations.Test;

/**
 *
 * @author Administrator
 */
public class ProcessTest {
    
    public static void main(String[] args){
        ProcessManager manager;
        AbProcess process1,process2,process3,process4;
        process1=new AbProcess(1L,"process1","process1"){
            @Override
            public boolean updateAct(long delttime) {
                System.out.println("update process2");
                return true;
            }
        };
        process2=new AbProcess(2L,"process2","process2"){
            @Override
            public boolean updateAct(long delttime) {
                System.out.println("update process2");
                return true;
            }
        };
        process3=new AbProcess(3L,"process3","process3"){
            @Override
            public boolean updateAct(long delttime) {
                System.out.println("update process3");
                return true;
            }
        };
        process4=new AbProcess(4L,"process4","process4"){
            @Override
            public boolean updateAct(long delttime) {
                System.out.println("update process4");
                return true;
            }
        };
        
        manager=new ProcessManager();
        process1.setNextProcess(process2);
        process2.setNextProcess(process4);
        
        manager.attachProcess(process1);
        manager.attachProcess(process3);        

        manager.updateAllProcess(30);
        
        AbProcess process=manager.findProcess(process1.getProcessId());
        if(process!=null){
            process.updateProcess(30);
            process.exception("exception by define!");
        }
        
        manager.updateAllProcess(30);
        manager.updateAllProcess(30);
    }
}
