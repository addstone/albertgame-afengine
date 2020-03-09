## 1.本类、接口、枚举名称
# App 游戏应用类[抽象类]  

## 2.本类设计目的
设计为游戏应用的主要程序入口。定义游戏应用的生命周期。    

## 3.本类的职责概要
一个游戏应用的主要入口，负责游戏应用与和插件部分管理器、游戏逻辑的生命周期。提供一个主要的抽象为具体的游戏做铺垫。  

## 4.本类的使用方式和生命周期
### 使用方式
游戏应用类，是游戏的主要程序入口。如果需要运行一个自定义的游戏，则需要创建一个游戏应用类的实例，和一个游戏逻辑的实例，（可以再创建一系列的插件部分实例，并且在运行之前便注入游戏应用类的插件部分管理器中），最后调用run接口并以游戏逻辑的实例运行游戏应用。  

```
	App app = new App1();//假设App1是一个App抽象的实现
	
	AppLogic logic1=new AppLogic1();//假设AppLogic1是一个AppLogic接口的实现

	PartSupport part1=new PartSupport1();//假设PartSupport1是一个PartSupport1接口的实现

	app.getPartSupportManager().addPart(part1);//预添加part到游戏应用实例中的插件部分管理器内

	app.run(logic);//以logic作为游戏逻辑，运行游戏应用
```

### 生命周期
游戏应用类的生命周期由游戏应用实例自己管理，在进行run之后，游戏应用，应用内部便会调用游戏应用类的init方法初始化具体游戏应用，随后调用插件部分管理器的初始化全部插件部分，随后初始化游戏逻辑，便进入游戏循环，不间断地进行：游戏逻辑更新、游戏插件部分管理器更新、游戏应用类更新，直到游戏循环被退出，调用游戏逻辑关闭->调用游戏插件部分管理器关闭->调用游戏应用类关闭，最终 退出游戏应用的运行。

## 5.本类需要实现的标准api说明

	App(appType,appName)		- 构建游戏应用实例的构造，注入游戏应用类型和游戏应用名称
	string getAppType() 		- 获取游戏应用的类型，在实例创建时注入
	string getAppName() 		- 获取游戏应用的类型，在实例船舰时注入

	string getValue(key)		- 获取游戏应用保存的信息
	void   setValue(key,value)	- 设置游戏应用保存的信息

	PartSupportManager getPartSupportManager() - 获取游戏应用插件部分管理器，在游戏应用的实例创建时自动创建
	MessageManager 	   getMessageManager()	- 获取游戏应用的消息管理器，在游戏应哟个的实例创建时自动创建
	void   init(logic)	- 以logic作为游戏逻辑，并且做好运行准备
	void   update(delttime)		- 更新应用所需要进行的流程，使用此方法，内部会调用逻辑，插件部分管理器，更新消息管理器，和具体应用的更新方法
	void   end()				- 结束应用应当进行的流程，
	void   exitAppUpdate()		- 退出游戏循环


	bool   initApp()			- 私有或者保护方法，由init(logic)调用，初始化游戏应用，[需由实现类实现]
	bool   updateApp(delttime)  - 私有或者保护方法，由update调用，更新游戏应用，[需由实现类实现]
	bool   shutdownApp()		- 私有或者保护方法，由end调用，关闭游戏应用，[需由实现类实现]	

	static App getRunningApp()	- 获取正在运行的游戏应用

## 6.相关其他类或者标准
### PartSupportManager
### MessageManager
### AppLogic
