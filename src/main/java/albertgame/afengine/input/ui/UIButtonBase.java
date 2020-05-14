package albertgame.afengine.input.ui;

import albertgame.afengine.app.message.Message;
import albertgame.afengine.input.InputServlet;
import albertgame.afengine.input.UIActor;
import albertgame.afengine.input.UIFace;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.math.Vector;

public class UIButtonBase extends UIActor{
    
    public static final int DOWN=0;
    public static final int NORMAL=1;
    public static final int COVER=2;

    protected int buttonState;
    
    private IUIAction toDownAction;
    private IUIAction toCoverAction;
    private IUIAction toNormalAction;    
    
    public UIButtonBase(String name, Vector pos) {
        super(name, pos);
        buttonState=NORMAL;
    }

    public IUIAction getToDownAction() {
        return toDownAction;
    }

    public void setToDownAction(IUIAction toDownAction) {
        this.toDownAction = toDownAction;
    }

    public IUIAction getToCoverAction() {
        return toCoverAction;
    }

    public void setToCoverAction(IUIAction toCoverAction) {
        this.toCoverAction = toCoverAction;
    }

    public IUIAction getToNormalAction() {
        return toNormalAction;
    }

    public void setToNormalAction(IUIAction toNormalAction) {
        this.toNormalAction = toNormalAction;
    }

    @Override
    protected void loadInToFace(UIFace face) {
        DebugUtil.log("loadin to face");
        face.addMsgUiMap(InputServlet.EventCode_MouseDown,this);
        face.addMsgUiMap(InputServlet.EventCode_MouseUp,this);
        face.addMsgUiMap(InputServlet.EventCode_MouseMove,this);                                
    }

    @Override
    protected void loadOutFromFace(UIFace face) {
        DebugUtil.log("loadout from face");
        face.removeMsgUiMap(InputServlet.EventCode_MouseDown,this);
        face.removeMsgUiMap(InputServlet.EventCode_MouseUp,this);
        face.removeMsgUiMap(InputServlet.EventCode_MouseMove,this);                                
    }
    
    
    //need override these three methods
    protected void doCover(){}
    protected void doNormal(){}
    protected void doDown(){}
    
    
    public void cover(){
        if(buttonState!=COVER){
            buttonState=COVER;
            if(toCoverAction!=null){
                toCoverAction.action(this);
            }
            doCover();            
        }
    }
    public void down(){
        if(buttonState!=DOWN){
            buttonState=DOWN;
            if(toDownAction!=null){
                toDownAction.action(this);
            }
            doDown();
        }
    }
    public void normal(){
        if(buttonState!=NORMAL){
            buttonState=NORMAL;
            if(toNormalAction!=null){
                toNormalAction.action(this);
            }            
            doNormal();                
        }
    }    
    //msg.extraobj[0]=mouseevent
    //INPUT_MOUSE_DOWN,INPUT_MOUSE_UP,INPUT_MOUSE_MOVE
    @Override
    public boolean handle(Message msg){
        
        int mx=(int) msg.extraObjs[0];
        int my=(int) msg.extraObjs[1];
        boolean in=this.isPointInUi(mx, my); 
        long type=msg.msgType;        
        if(type==InputServlet.EventCode_MouseDown){
            return handleDown(in);
        }else if(type==InputServlet.EventCode_MouseUp){
            return handleUp(in,mx,my);
        }else if(type==InputServlet.EventCode_MouseMove){
            return handleMove(in);
        }else;
        
        return false;  
    }    
    //msgextra[0]=mouseevent
    private boolean handleDown(boolean in){
        if(in){
            down();
            return false;//让其他UI继续处理，比如按钮的释放
        }
        return false;
    }
    private boolean handleUp(boolean in,int mx,int my){        
        if(in){
            if(buttonState==DOWN)
                cover();
                return false;//让其他的UI继续处理MouseUp，比如按钮
        }else{
            if(buttonState==DOWN)
                normal();
            return false;
        }
    }
    
    private boolean handleMove(boolean in){
        if(!in&&buttonState==COVER){
            normal();
        }
        if(in&&buttonState==NORMAL){
            cover();
        }

        //cosume
        return false;
    }
    
}
