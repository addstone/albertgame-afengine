/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author albert-flex
 */
public class ProcessManager {

    private final List<AbProcess> processList;
    private final List<AbProcess> waitforAdd;
    private final List<AbProcess> waitforRemove;

    public ProcessManager() {
        processList = new ArrayList<>();
        waitforAdd = new ArrayList<>();
        waitforRemove = new ArrayList<>();
    }

    public void attachProcess(AbProcess process) {
        if (!containsProcess(process.getProcessId())) {
            waitforAdd.add(process);
        } else {
            System.out.println("Add Process " + process.getProcessName() + " is failed.. already has this process...");
        }
    }

    public AbProcess findProcess(long processId) {
        Iterator<AbProcess> processiter = processList.iterator();
        while (processiter.hasNext()) {
            AbProcess process = processiter.next();
            if (process.getProcessId() == processId) {
                return process;
            }
        }
        return null;
    }

    public boolean containsProcess(long processId) {
        Iterator<AbProcess> processiter = processList.iterator();
        while (processiter.hasNext()) {
            AbProcess process = processiter.next();
            if (process.getProcessId() == processId) {
                return true;
            }
        }
        return false;
    }

    public void updateAllProcess(long time) {
        Iterator<AbProcess> processIter = processList.iterator();
        while (processIter.hasNext()) {
            AbProcess process = processIter.next();
            handleProcess(process, time);
        }

        Iterator<AbProcess> waitremoveiter = waitforRemove.iterator();
        while (waitremoveiter.hasNext()) {
            AbProcess process = waitremoveiter.next();
            processList.remove(process);
        }

        Iterator<AbProcess> waitadditer = waitforAdd.iterator();
        while (waitadditer.hasNext()) {
            AbProcess process = waitadditer.next();
            processList.add(process);
        }

        //clear for wait..
        waitforAdd.clear();
        waitforRemove.clear();
    }

    private void handleProcess(AbProcess process, long time) {
        process.updateProcess(time);
        if (process.getState() == AbProcess.State.COULD_REMOVE) {
            waitforRemove.add(process);
            if(process.getNextProcess()!=null){
                waitforAdd.add(process.getNextProcess());
            }
        }
    }
}
