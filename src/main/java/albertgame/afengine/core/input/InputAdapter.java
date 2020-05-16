package albertgame.afengine.core.input;

import albertgame.afengine.core.message.Message;
import albertgame.afengine.core.message.MessageManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class InputAdapter implements KeyListener, MouseListener,
        MouseMotionListener, MouseWheelListener, WindowListener {

    private void sendMsg(long type, Object ... obj) {
        Message msg = new Message(InputRoute.Route_Input, type, 0, "",obj);
        MessageManager.getInstance().pushMessage(msg);
    }

    private void sendMsg(long type) {
        Message msg = new Message(InputRoute.Route_Input, type, 0, "", new Object[]{});
        MessageManager.getInstance().pushMessage(msg);
    }

    //content-eventcode,exobj[keychar]
    @Override
    public void keyTyped(KeyEvent e) {
        //DebugUtil.log("key typed " + e.getKeyChar());
//        System.out.println("key type");
        sendMsg(InputServlet.EventCode_KeyType, e.getKeyChar());
    }

    //content-eventcode,exobj[keycode]
    @Override
    public void keyPressed(KeyEvent e) {
        //DebugUtil.log("key pressed " + e.paramString());
        sendMsg(InputServlet.EventCode_KeyDown, e.getKeyCode());
    }

    //content-eventcode,exobj[keycode]
    @Override
    public void keyReleased(KeyEvent e) {
        //DebugUtil.log("key released " + e.paramString());
        sendMsg(InputServlet.EventCode_KeyUp,e.getKeyCode());
    }

    //content-eventcode,exobj[mousex,mousey]
    @Override
    public void mouseClicked(MouseEvent e) {
        //DebugUtil.log("mouse clicked " + e.paramString());
        sendMsg(InputServlet.EventCode_MouseClick, e.getX(), e.getY(),e.getButton());
    }

    //content-eventcode,exobj[mousex,mousey,keycode]
    @Override
    public void mousePressed(MouseEvent e) {
        //DebugUtil.log("mouse pressed " + e.paramString());
        sendMsg(InputServlet.EventCode_MouseDown, e.getX(), e.getY(), e.getButton());
    }

    //content-eventcode,exobj[mousex,mousey,keycode]
    @Override
    public void mouseReleased(MouseEvent e) {
        //DebugUtil.log("mouse released " + e.paramString());
        sendMsg(InputServlet.EventCode_MouseUp, e.getX(), e.getY(), e.getButton());
    }

    //content-eventcode
    @Override
    public void mouseEntered(MouseEvent e) {
        //DebugUtil.log("mouse enter windows");
        sendMsg(InputServlet.EventCode_MouseInWindow);
    }

    //content-eventcode
    @Override
    public void mouseExited(MouseEvent e) {
        //DebugUtil.log("mouse exit windows");
        sendMsg(InputServlet.EventCode_MouseExitWindow);
    }

    //content-eventcode,exobjs[mousex,mousey]
    @Override
    public void mouseDragged(MouseEvent e) {
        //DebugUtil.log("mouse dragged to [" + e.getX() + "," + e.getY() + "]");
        sendMsg(InputServlet.EventCode_MouseDrag, e.getX(), e.getY());
    }

    //content-eventcode,exobjs[mousex,mousey]
    @Override
    public void mouseMoved(MouseEvent e) {
//        //DebugUtil.log("mouse moved to ["+e.getX()+","+e.getY()+"]") ;
        sendMsg(InputServlet.EventCode_MouseMove, e.getX(), e.getY());
    }

    //content-eventcode
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int r = e.getWheelRotation();
        if (r > 0) {
            //DebugUtil.log("mousewheel up");
            sendMsg(InputServlet.EventCode_MouseWheelUp);
        } else if (r < 0) {
            //DebugUtil.log("mousewheel down");
            sendMsg(InputServlet.EventCode_MouseWheelDown);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        //DebugUtil.log("window opened");
//        sendMsg(InputServlet.EventCode_WindowOpened);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        //DebugUtil.log("window closing");
//        sendMsg(InputServlet.EventCode_WindowClosing);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //DebugUtil.log("window closed");
//        sendMsg(InputServlet.EventCode_WindowClose);
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //DebugUtil.log("window iconified");
        sendMsg(InputServlet.EventCode_WindowIconed);
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //DebugUtil.log("window deiconified");
        sendMsg(InputServlet.EventCode_WindowDeiconed);
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //DebugUtil.log("window actived");
        sendMsg(InputServlet.EventCode_WindowActive);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //DebugUtil.log("window deactived");
        sendMsg(InputServlet.EventCode_WindowDeactive);
    }
}
