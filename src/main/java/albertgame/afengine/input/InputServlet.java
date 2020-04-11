package albertgame.afengine.input;

import albertgame.afengine.util.math.IDGenerator;

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
     * char ascii: 32 - 126 key tab caplock numlock shift ctrl fn alt insert delete
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
    public static long Route_Input=new IDGenerator().createId();
    
    public static long EventCode_KeyDown=0;
    public static long EventCode_KeyUp=1;
    public static long EventCode_KeyType=2;
    
    public static long EventCode_MouseDown=3;
    public static long EventCode_MouseUp=4;
    public static long EventCode_MouseClick=5;
    public static long EventCode_MouseDrag=6;
    public static long EventCode_MouseWheelUp=16;
    public static long EventCode_MouseWheelDown=17;
    
    public static long EventCode_WindowOpened=7;
    public static long EventCode_WindowClose=8;
    public static long EventCode_WindowClosing=9;
    public static long EventCode_WindowIconed=10;
    public static long EventCode_WindowDeiconed=11;
    public static long EventCode_WindowActive=12;
    public static long EventCode_WindowDeactive=13;
    public static long EventCode_WindowMouseEnter=14;
    public static long EventCode_WindowMouseExit=15;
    
    public static long KeyCode_Tab;
    public static long KeyCode_Shift;
    public static long KeyCode_Ctrl;
    public static long KeyCode_Alt;
    public static long KeyCode_Insert;
    public static long KeyCode_Delet;
    public static long KeyCode_PageUp;
    public static long KeyCode_PageDown;

    public static long KeyCode_Up;
    public static long KeyCode_Down;
    public static long KeyCode_Left;
    public static long KeyCode_Right;

    public static long KeyCode_Home;
    public static long KeyCode_End;
    public static long KeyCode_Enter;
    public static long KeyCode_BackSpace;
    public static long KeyCode_Esc;
    
    public static long CharCode(char word){
        return word;
    }
}
