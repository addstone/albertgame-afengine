package albertgame.afengine.input;

import albertgame.afengine.app.App;
import albertgame.afengine.app.message.Message;
import albertgame.afengine.app.message.Message.IHandler;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Administrator
 */
public class InputServlet {
    /**
     * 对象会设置输入模块的相关静态数据，比如按键的映射，鼠标的映射，和其他相关的东西，并且会在按键、鼠标操作时，发送输入模块的消息，以提供给输入模块使用。
     * 会初始化InputServlet中的所有的事件数据，包括如下 键盘的映射 
     * ```
     * event: KeyDown,KeyUp,KeyType
     * char ascii: A~Z key tab caplock numlock shift ctrl fn alt insert delete
     * prt enter up down left right pageup pagedown home end f1-f12 esc
     * ```
     * event的映射，可以使用InputServlet.EventCode_*;
     * char的映射,可以使用InputServlet.CharCode('char')来获取相关的字符转换为键盘映射
     * key的映射,可以使用InputServlet.KeyCode_* 来获取相关的转换为Int的键盘映射      *
     * 鼠标的映射 
     * ```
     * event: MouseDown,MouseUp,MouseClick,MouseDrag,
     * MouseWheelUp,MouseWheelDown, button mouse_left,mouse_center,mouse_right
     * ``` 
     * event的映射，如InputServlet.EventCode_*;
     * button的映射，如InputServlet.MouseButtonCode_*;Left,Center,Right
     * 
     * event:WindowOpened,WindowClose,WindowClosing,WindowIconed,WindowDeiconde,
     *       WindowActive,WindowDeactive,WindowMouseEnter,WindowMouseExit
     * event的映射，如InputServlet.EventCode_*
     */
    
    public static final  long EventCode_KeyDown=0;
    public static final  long EventCode_KeyUp=1;
    public static final  long EventCode_KeyType=2;
    
    public static final  long EventCode_MouseDown=3;
    public static final  long EventCode_MouseUp=4;
    public static final  long EventCode_MouseClick=5;
    public static final  long EventCode_MouseDrag=6;
    public static final  long EventCode_MouseWheelUp=16;
    public static final  long EventCode_MouseWheelDown=17;
    public static final  long EventCode_MouseInWindow=18;
    public static final  long EventCode_MouseExitWindow=19;
    public static final  long EventCode_MouseMove=20;
    
    public static final  long EventCode_WindowOpened=7;
    public static final  long EventCode_WindowClose=8;
    public static final  long EventCode_WindowClosing=9;
    public static final  long EventCode_WindowIconed=10;
    public static final  long EventCode_WindowDeiconed=11;
    public static final  long EventCode_WindowActive=12;
    public static final  long EventCode_WindowDeactive=13;
    public static final  long EventCode_WindowMouseEnter=14;
    public static final  long EventCode_WindowMouseExit=15;
    
    public static final  long KeyCode_Tab=KeyEvent.VK_TAB;
    public static final  long KeyCode_Shift=KeyEvent.VK_SHIFT;
    public static final  long KeyCode_Ctrl=KeyEvent.VK_CONTROL;
    public static final  long KeyCode_Alt=KeyEvent.VK_ALT;
    public static final  long KeyCode_Insert=KeyEvent.VK_INSERT;
    public static final  long KeyCode_Delete=KeyEvent.VK_DELETE;
    public static final  long KeyCode_PageUp=KeyEvent.VK_PAGE_UP;
    public static final  long KeyCode_PageDown=KeyEvent.VK_PAGE_DOWN;

    public static final  long KeyCode_Up=KeyEvent.VK_UP;
    public static final  long KeyCode_Down=KeyEvent.VK_DOWN;
    public static final  long KeyCode_Left=KeyEvent.VK_LEFT;
    public static final  long KeyCode_Right=KeyEvent.VK_RIGHT;

    public static final  long KeyCode_Home=KeyEvent.VK_HOME;
    public static final  long KeyCode_End=KeyEvent.VK_END;
    public static final  long KeyCode_Enter=KeyEvent.VK_ENTER;
    public static final  long KeyCode_BackSpace=KeyEvent.VK_BACK_SPACE;
    public static final  long KeyCode_Esc=KeyEvent.VK_ESCAPE;
    
    public static final  long MouseButton_Left=MouseEvent.BUTTON1;
    public static final  long MouseButton_Center=MouseEvent.BUTTON2;
    public static final  long MouseButton_Right=MouseEvent.BUTTON3;
    
    //just for A~Z ; , . / [ ] \  - = 0~9
    public static final  long CharCode(char word){
        return word;
    }
        
    private final long handleType;
    private final IHandler handler;
    public final String servletName;
    public static final InputServlet ExitServlet=new InputServlet(EventCode_KeyUp,"exit",new ExitHandler());

    public static class ExitHandler implements IHandler{
        @Override
        public boolean handle(Message msg){
            int keycode=(int)msg.extraObjs[0];
            if(keycode==InputServlet.KeyCode_Esc){
                App.exit();
                return true;
            }
            return false;
        }
    }
    
    public InputServlet(long type,String name,IHandler handler){
        this.handleType=type;
        this.handler=handler;
        this.servletName=name;
    }
    
    public final long getHandleType() {
        return handleType;
    } 

    public final IHandler getHandler() {
        return handler;
    }
    
    public final boolean handle(Message msg){
        if(handler!=null){
            return handler.handle(msg);
        }
        return false;
    }    
}
