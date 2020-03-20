# 对象名称
## InputServlet 输入过滤器

# 对象介绍
输入过滤器会对流过的事件做处理判断，如果处理成功，则返回true，以免让其他输入过滤器处理。  
内部包含一些键盘的映射，直接可以使用char字符来显示这些键盘映射。

# 对象功能

对象会设置输入模块的相关静态数据，比如按键的映射，鼠标的映射，和其他相关的东西，并且会在按键、鼠标操作时，发送输入模块的消息，以提供给输入模块使用。
会初始化InputServlet中的所有的事件数据，包括如下  
键盘的映射
```
event
KeyDown,KeyUp,KeyType
char
ascii: 32 - 126
key
tab caplock numlock shift ctrl fn alt insert delete prt enter
up down left right pageup pagedown home end
f1-f12 esc
```
event的映射，可以使用InputServlet.EventCode_*;
char的映射,可以使用InputServlet.CharCode('char')来获取相关的字符转换为键盘映射  
key的映射,可以使用InputServlet.KeyCode_* 来获取相关的转换为Int的键盘映射  

鼠标的映射  
```
event
MouseDown,MouseUp,MouseClick,MouseDrag,
MouseWheelUp,MouseWheelDown,
button
mouse_left,mouse_center,mouse_right
```
event的映射，如InputServlet.EventCode_*;
button的映射，如InputServlet.MouseButtonCode_*;
获取按钮的位置,如InputServlet.MousePosX,MousePosY;
# 对象相关类
## InputAdapter