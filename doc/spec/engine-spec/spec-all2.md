# afengine v1.0.0 特性标准文档

albertgame.afengine

# 特性支持的相关类和接口
'->' 表示实现类,
':' 表示包含一系列模块
app：
App -> ServiceApp,(WindowApp -> SimpleWindowApp)
AppLogic
AppLogic.IFactory //创建AppLogic接口
AppBoot

+++

app.message:
Message
MessageHandler
MessageHandler.IFactory //创建消息处理器接口
MessageRoute.IFactory //创建消息路由器接口
MessageRoute -> MessageHandlerRoute,(UIInputMessageRoute)
MessageManager

+++

graphics:
IGraphicsDraw
IGraphicsState
IGraphicsCreate
IColor/IFont/ITexture
IWindowAdjustListener
IDrawStrategy
IDrawStrategy.IFactory //创建渲染策略接口
IGraphicsTech.IFactory //创建图形技术引擎的接口
IGraphicsTech -> GraphicsTech_Java2D

+++

graphics.screen:
IScreenElement.IDraw, //子屏幕的渲染策略
IScreenElement.IDraw.IFactory // 创建子屏幕渲染的接口
IScreenElement.Camera //子屏幕的照相机映射
IScreenElement //含有一个draw
ScreenHolder, 
ScreenRenderStrategy //包含一个屏幕元素渲染器，一个beforedraw和一个afterdraw

+++

scene:
Scene
SceneLoader
SceneLoader.IFactory //创建场景引导器的接口
SceneFileHelp
Actor
ActorComponent
ActorComponent.IFactory //创建实体组件的接口

+++

scene.actorcomponent:
Render -> TextRender,TextureRender,LineRender,PolygonRender,OvalRender
BehaviorBean: ActorBehavior,ActorBehavior.IFactory //组件行为的工厂
Action: ActorAction,FrameAnimAction,TimerAction,TimerAction.ICallAct,TimerAction.ICallAct.IFactory 延时回调动作工作接口

+++

uiinput:
InputAdapter
InputServlet
UIInputMessageRoute
UIInputCenter
UIInputFileHelp //输入配置文件

+++

uiinput.control:
UIAction.IFactory //UI动作工厂
UIAction
UIButtonBase -> UIImageButton,UITextButton,UIImageToggle
UIImageView
UIText
UIInputArea -> UIInputLine
UIFace //UI界面容器
UIViewList -> UIButtonList

+++

util:
DebugUtil
AssetsUtil
SoundUtil
XMLUtil
FactoryCenter
FactoryInterface

+++

util.process:
Process.State
Process.IFactory //进程工厂
Process
ProcessManager

+++

util.math:
Vector
Transform
IDGenerator

+++

util.property:
ValueProperty<T>.IChange
ValuePropertyBind<T>
ValueProperty<T>
ValuePropertyList<T>.IChange
ValuePropertyList<T>
LongProperty,IntProperty,DoubleProperty,StringProperty
