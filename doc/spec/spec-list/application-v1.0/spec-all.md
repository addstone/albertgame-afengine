# applicatin v1.0 特性标准文档

# 特性说明
## 提供游戏应用全局的应用设置访问，以及游戏应用入口，游戏内置的一些功能。可以使用外部创建的游戏逻辑作为引擎运行额外的逻辑运行，也可以单纯地依靠引擎的运行流程运行。是游戏应用引擎的核心部分，并且内部提供窗口接口，以提供外部可切换的渲染实现。

# 特性支持的相关对象

## 应用入口部分
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
### GraphicsTech 图形技术具体接口，继承GraphicsDraw\Create\State三个接口，并且拥有一个图形渲染策略的实例
### RenderStrategy 内置的图形渲染策略实现

## 核心集成部分-part
### PartSupport 自我管理部分支持
### PartSupportManager 自我管理部分支持管理器

## 核心集成部分-message
### Message 应用消息
### MessageCenter  应用消息中心
### MessageRoute 应用消息路由器
### MessageHandlerRoute 内置消息路由器实现，类型-内容消息发送路由器
### MessageHandler 应用消息处理器