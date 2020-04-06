package albertgame.afengine.uiinput;

import albertgame.afengine.util.DebugUtil;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class InputAdapter implements KeyListener,MouseListener,
        MouseMotionListener,MouseWheelListener,WindowListener{

    @Override
    public void keyTyped(KeyEvent e) {
        DebugUtil.log("key typed "+e.getKeyChar());
//        sendMsg(InputServlet.INPUT_KEY_TYPE,e);
    }
    private void sendMsg(long type,Object obj){
//         Message msg=new Message(UIInputRoute.ROUTE_ID,type,0,"",new Object[]{obj},
//                System.currentTimeMillis(),0,0,null,0,0,0);
//         MessageCenter.getInstance().sendMessage(msg);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        DebugUtil.log("key pressed "+e.paramString());
//        sendMsg(InputServlet.INPUT_KEY_DOWN,e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        DebugUtil.log("key released "+e.paramString());
//        sendMsg(InputServlet.INPUT_KEY_UP,e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        DebugUtil.log("mouse clicked "+e.paramString());
//        sendMsg(InputServlet.INPUT_MOUSE_CLICK,e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        DebugUtil.log("mouse pressed "+e.paramString());
//        sendMsg(InputServlet.INPUT_MOUSE_DOWN,e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        DebugUtil.log("mouse released "+e.paramString());
//        sendMsg(InputServlet.INPUT_MOUSE_UP,e);
    }

    //do not 
    @Override
    public void mouseEntered(MouseEvent e){
        DebugUtil.log("mouse enter windows");
//        sendMsg(InputServlet.INPUT_WINDOW_MOUSEENTER,e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        DebugUtil.log("mouse exit windows");
//        sendMsg(InputServlet.INPUT_WINDOW_MOUSEEXIT,e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        DebugUtil.log("mouse dragged to ["+e.getX()+","+e.getY()+"]") ;
//        sendMsg(InputServlet.INPUT_MOUSE_DRAG,e);        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        DebugUtil.log("mouse moved to ["+e.getX()+","+e.getY()+"]") ;
//        sendMsg(InputServlet.INPUT_MOUSE_MOVE,e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        DebugUtil.log("mouse wheel ["+e.getWheelRotation()+"]") ;
//        sendMsg(InputServlet.INPUT_MOUSEWHEEL,e);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        DebugUtil.log("window opened") ;
//        sendMsg(InputServlet.INPUT_WINDOW_OPENED,e);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        DebugUtil.log("window closing") ;
//        sendMsg(InputServlet.INPUT_WINDOW_CLOSING,e);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        DebugUtil.log("window closed") ;
//        sendMsg(InputServlet.INPUT_WINDOW_CLOSE,e);
    }

    @Override
    public void windowIconified(WindowEvent e) {
        DebugUtil.log("window iconified") ;
//        sendMsg(InputServlet.INPUT_WINDOW_ICONED,e);
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        DebugUtil.log("window deiconified") ;
//        sendMsg(InputServlet.INPUT_WINDOW_DEICONED,e);
    }

    @Override
    public void windowActivated(WindowEvent e) {
        DebugUtil.log("window actived") ;
//        sendMsg(InputServlet.INPUT_WINDOW_ACTIVE,e);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        DebugUtil.log("window deactived") ;
//        sendMsg(InputServlet.INPUT_WINDOW_DEACTIVE,e);
    }    
}
