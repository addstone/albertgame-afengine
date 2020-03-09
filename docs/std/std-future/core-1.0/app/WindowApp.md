## 1.本类、接口、枚举名称
# WindowApp 窗口游戏应用类  

## 2.本类设计目的
App游戏应用提供的标准与window提供的标准实现。支持窗口游戏应用。

## 3.本类的职责概要
作为窗口和渲染资源的游戏应用实现。重写游戏应用的initApp方法，

## 4.本类的使用方式和生命周期
### 使用方式
见App标准  
其他:可以调用windows的GraphicsTech图形技术接口，进行渲染工作。

### 生命周期
见App标准

## 5.本类需要实现的标准api说明

	见App标准  
	WindowApp(appName,graphicsTech)		- 构建窗口游戏应用
	GraphicsTech getGraphicsTech() 		- 	获取构建此窗口游戏应用实例的窗口图形技术实现类对象

## 6.相关其他类或者标准
### App
### GraphicsTech