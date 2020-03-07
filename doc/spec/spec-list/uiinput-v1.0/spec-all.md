# uiinput v1.0 特性标准文档

# 特性说明
UI输入标准。包含键盘、鼠标的输入管理，与UI控件的显示。

# 特性支持的相关对象

## 输入-input
### InputAdapter 具体的实现以各个操作系统的事件处理为准
### InputServlet 输入服务
### UIInputMessageRoute 输入消息路由器
### UIInputCenter ui输入中心
### UIInputPartSupport ui输入分部支持
### UIInputFileHelp 输入配置文件帮助

## ui界面
### UIFace UI界面
### UIDrawStrategy UI界面的渲染策略
### UIActor UI控件基类
### UIFaceFileHelp 界面配置文件帮助

## ui控件
### UIAction UI控件动作
### UIActorFactory UI控件工厂

### UIButtonBase UI按钮
### UIImageButton 图片按钮
### UITextButton 文本按钮
### UIImageToggle 图片状态按钮

### UIImageLabel 图片标签
### UITextLabel 文本标签

### UIInputArea 输入框
### UIInputLine 输入栏

