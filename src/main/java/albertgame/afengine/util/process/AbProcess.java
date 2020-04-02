/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util.process;

/**
 *
 * @author Administrator
 */
public abstract class AbProcess {

    public static enum State {
        UNSETUP,
        SETUP,
        RUNNING,
        PAUSED,
        EXCEPTION,
        END_SUCCESS,
        END_FAILED,
        END_ABORT,
        COULD_REMOVE,
    }

    private final long processId;
    private final String processInfo;
    private final String processName;
    
    private State state;
    private String exceptionInfo;

    private AbProcess nextProcess=null;
    
    public AbProcess(long processId, String processName, String processInfo) {
        this.processId = processId;
        this.processInfo = processInfo;
        this.processName = processName;
        state = State.UNSETUP;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getProcessId() {
        return processId;
    }

    public String getProcessInfo() {
        return processInfo;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public String getProcessName() {
        return processName;
    }

    public AbProcess getNextProcess(){
        return nextProcess;
    }

    public void setNextProcess(AbProcess nextProcess){
        this.nextProcess = nextProcess;
    }
    
    

    public final void setUp() {
        if (state == State.UNSETUP) {
            if (!initAct()) {
                state = State.EXCEPTION;
                return;
            }
            state = State.RUNNING;
            updateAct(delt);
        }
    }

    long delt;

    public final void updateProcess(long delttime) {
        this.delt=delttime;
        switch (state) {
            case UNSETUP:
                setUp();
                break;
            case SETUP:
                state = State.RUNNING;
                break;
            case RUNNING:
                updateAct(delttime);	    	
                break;
            case END_SUCCESS:
                success();
                state = State.COULD_REMOVE;
                break;
            case END_FAILED:
                failed();
                state = State.COULD_REMOVE;
                break;
            case END_ABORT:
                abort();
                state = State.COULD_REMOVE;
                break;
            case EXCEPTION:
                exceptionAct(this.exceptionInfo);
                state = State.COULD_REMOVE;
                break;
            default:
                break;
        }
    }

    public final void pause() {
        if (state == State.RUNNING) {
            state = State.PAUSED;
            intoPauseAct();
        }
    }

    public final void resume() {
        if (state == State.PAUSED) {
            state = State.RUNNING;
            intoResumeAct();
        }
    }

    public final void success() {
        state = State.END_SUCCESS;
    }

    public final void failed() {
        state = State.END_FAILED;
    }

    public final void abort() {
        state = State.END_ABORT;
    }

    public final void exception(String info) {
        state = State.EXCEPTION;
        exceptionInfo = info;
    }

    public void sucessAct() {
    }

    public void failedAct() {
    }

    public void intoPauseAct() {
    }

    public void intoResumeAct() {
    }

    public void abortAct() {
    }

    public void exceptionAct(String exceptionInfo) {
    }

    public void setupAct() {
    }

    public boolean initAct(){
        return true;
    }

    public abstract void updateAct(long delttime);
    
}
