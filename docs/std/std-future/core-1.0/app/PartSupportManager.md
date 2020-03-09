## 1.本类、接口、枚举名称
# PartSupportManager 插件部分管理器类  

## 2.本类设计目的
为了解决游戏应用核心外围的插件部分的生命周期和优先顺序。通过调度插件部分的优先顺序，管理各项插件部分的初始化，更新，以及关闭的游戏顺序。解决了游戏插件部分一些细小部分的依赖问题，正确设置加入插件部分的顺序，或者优先顺序。  

## 3.本类的职责概要
外部和游戏应用的实例集成，内部管理添加的一些插件部分。游戏应用在初始化，更新和关闭实例的时候调用自带的插件管理器的初始化、更新、关闭所有的加入的游戏插件部分。  

## 4.本类的使用方式和生命周期
### 使用方式
插件部分管理器集成在游戏应用，游戏应用实例中有一个插件部分管理器的实例。在游戏运行初始化阶段的时候，将需要添加的插件部分添加到游戏应用插件部分管理器实例当中，全部添加完毕之后，游戏应用会自动按优先顺序初始化全部的插件部分(调用插件部分管理器的initAllParts方法)，随后便进入游戏循环，循环当中会调用游戏应用实例之中的管理器实例的updateAllParts方法，当游戏循环结束，游戏逻辑关闭结束之后，便会调用管理器的shutdownAllParts方法，按照优先顺序关闭所有的插件部分。  

使用接口，加入游戏插件部分  
```
	
	PartSupportManager manager;
	...//获取游戏应用实例中的插件部分管理器
	...//假设PartSupportA,PartSupportB,PartSupportC为实现了PartSupport接口的实现类
	PartSupport partA=new PartSupportA();
	PartSupport partB=new PartSupportB();
	PartSupport partC=new PartSupportB();
	manager.addPart(partA,1);//以1为优先级加入到管理器
	manager.addPart(partB,10);//以10为优先级加入到管理器
	manager.addPart(partC,3,false);//以3为优先级加入到管理器，并且指定无需更新该插件部分
```

### 生命周期
管理器已由游戏应用实例自行创建和管理一个特定的实例，或者也可以手动创建，自行管理实例。

## 5.本类需要实现的标准api说明

	bool addPart(partsupport,order);	- 以指定优先级加入管理器，设定为需要更新
	bool addPart(partsupport,order,needupdate)	- 以指定的优先级和是否需要更新为准，加入管理器
	bool hasPart(partname);				- 判断管理器内是否存在指定名称的插件部分
	bool removePart(partname);			- 移除指定名称的插件部分(如果有)
	partsupport getPart(partname);		- 获取指定名称的插件部分(如果有)

	int  getPartOrder(partname);		- 获取指定名称的插件部分的优先级(如果有)
	bool changePartOrder(partname,newOrder) - 改变指定名称的插件部分的优先级(如果有)

	initAllParts();						- 按优先级初始化所有的已添加的插件部分
	updateAllParts(delttime);		    - 按优先级更新所有的已添加的插件部分
	shutdownAllParts();					- 按优先级关闭所有的已添加的插件部分

## 6.相关其他类或者标准
### PartSupport