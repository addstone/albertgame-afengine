# applicatin v1.0 特性标准文档

# 特性支持的相关类和接口
'->' 表示实现类,
根包下：
App -> ServiceApp,(WindowApp -> SimpleWindowApp)
AppLogic

graphics包下:
IGraphicsTech.IDrawMethod
IGraphicsTech.IState
IGraphicsTech.ICreate
IGraphicsTech.IColor
IGraphicsTech.IFont
IGraphicsTech.ITexture
IGraphicsTech.IWindowAdjustListener
IGraphicsTech.IDrawStrategy
IGraphicsTech -> GraphicsTech_Java2D

graphics.screen 包下:
IScreenElement.IDraw, //子屏幕的渲染策略
IScreenElement.Camera //子屏幕的照相机映射
IScreenElement //含有一个draw
ScreenHolder, 
ScreenRenderStrategy 




## 应用入口部分-entry
### Application 游戏应用抽象实体
### ServiceApplication 服务游戏应用，抽象游戏应用的实现体
### WindowApplication 窗口游戏应用，抽象游戏应用的的实现体
### ApplicationLogic 游戏逻辑接口

## 核心集成部分-graphics
### GraphicsDraw 图形引擎绘制接口
### GraphicsCreate 图形引擎创建接口
### GraphicsState 图形引擎状态接口
### Color\Font\Texture 颜色接口\字体接口\纹理接口
### DrawStratey 图形渲染策略
### GraphicsTech 图形技术具体接口，继承Draw\Create\State三个接口，并且拥有一个图形渲染策略的实例
### RenderStrategy 内置的图形渲染策略实现

## 核心集成部分-part
### PartSupport 自我管理部分支持
### PartSupportManager 自我管理部分支持管理器

## 核心集成部分-message
### Message 应用消息
### MessageManager  应用消息管理器
### MessageRoute 应用消息路由器
### MessageHandlerRoute 内置消息路由器实现，类型-内容消息发送路由器
### MessageHandler 应用消息处理器