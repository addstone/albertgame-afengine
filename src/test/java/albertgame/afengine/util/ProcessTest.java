/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import albertgame.afengine.util.process.AbProcess;
import albertgame.afengine.util.process.ProcessManager;
import org.testng.annotations.Test;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ProcessTest {

    private static final Logger log = Logger.getLogger(ProcessTest.class.getName());
    
    @Test
    public void testProcess(){
        ProcessManager manager;
        AbProcess process1,process2,process3,process4;
        process1=new AbProcess(1L,"process1","process1"){
            @Override
            public void updateAct(long delttime) {
                log.log(Level.INFO,"update process1");
            }
        };
        process2=new AbProcess(2L,"process2","process2"){
            @Override
            public void updateAct(long delttime) {
                log.log(Level.INFO,"update process2");
            }
        };
        process3=new AbProcess(3L,"process3","process3"){
            @Override
            public void updateAct(long delttime) {
                log.log(Level.INFO,"update process3");
            }
        };
        process4=new AbProcess(4L,"process4","process4"){
            @Override
            public void updateAct(long delttime) {
                log.log(Level.INFO,"update process4");
            }
        };
        
        process1.updateProcess(30);
        process3.updateProcess(30);

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
