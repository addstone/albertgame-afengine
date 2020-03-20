# scene v1.0 特性标准文档

# 特性说明
场景/实体/组件标准，以场景为单位管理实体的内容。

# 特性支持的相关对象

## Scene
## SceneLoader
## SceneFileHelp

## Actor 实体
## ActorComponent 实体组件
## ComponentFactory 组件工厂

## 内部实现:
### RenderComponent
	TextRenderComponent
	TextureRenderComponent
	LineRenderComponent
	PolygonRenderComponent
### BehaviorBeanComponent(ActorBehavior)
### ActionComponent(FrameAnimAction,TimerAction)