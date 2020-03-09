# 对象名称
## InputAdapter 输入适配器

# 对象介绍
输入适配器是为了适配不同操作系统或者底层软件系统所提供的不同的输入系统。具体需要由具体的操作系统来应付。

# 对象功能

对象会设置输入模块的相关静态数据，比如按键的映射，鼠标的映射，和其他相关的东西，并且会在按键、鼠标操作时，发送输入模块的消息，以提供给输入模块使用。
会初始化InputServlet中的所有的事件数据，包括如下
键盘的映射
```
char
~ · ! @ # $ % ^ & * ( ) - _ + = [ ] { } \ |
a-z A-Z 0-9 , . : ; " ' < > / ?
key
tab caplock numlock shift ctrl fn alt insert delete prt enter
up down left right pageup pagedown home end
f1-f12 esc
mouse_left,mouse_center,mouse_right
```
# 对象相关类
## InputServlet