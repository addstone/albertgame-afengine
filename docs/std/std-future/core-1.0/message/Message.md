## 1.本类、接口、枚举名称
# Message 消息类

## 2.本类设计目的
设计为游戏引擎内置的消息处理所支持的消息对象，用来表示一个消息的主要内容,消息的内部的数据是否填满需要观察路由转发器是否需要处理这些内部的数据,路由转发类型则是必须确认的，否则将无法转发消息。消息为引擎内核与外围的部分或者游戏逻辑之间的交互。  

	消息所需的路由转发类型,  
	消息的类型,  
	消息的内容,  
	消息的提示,  
	消息的附加内容,  

	消息的发生时间戳,  
	消息将要延迟发送的延迟时间,  
	发送方的类型,  
	发送方的主体,  

	接收方的类型,  
	发送方的id,  
	接收方的id,  

## 3.本类的职责概要
需要为消息发送方和接受方提供一个可以解耦和的桥梁。通过消息类，可以将发送者与处理着进行解耦。

## 4.本类的使用方式和生命周期
### 使用方式
获取一个消息管理器，将消息推入消息管理器的消息队列之中，消息管理器会自动在其生命周期之中处理这个消息。
```
	MessageManager manager;
	...//获取manager
	Message msg;
	...//填入需要的数据,
	manager.pushMessage(msg);

	//当调用消息管理器的更新方法时，将会处理这样的消息
	manager.updateQueue();
	//如果内部有消息路由器注册，并且转发了消息之后，获得了true的返回值，则表示消息处理完成，否则认为无法处理并丢弃这个消息
```

### 生命周期
创建之后，自行管理，如果推入消息管理器的话，会由消息管理器自动在其updateQueue方法调用时分发，当处理完毕，或者无法处理的时候，将会移除消息对象。

## 5.本类需要实现的标准api说明
	
	Message(routeType,msgType,msgContent,msgInfo,extraObjs,
	timeTamp,delayTime,senderType,senderObj,
	receiveType,senderId,receiveId)

	Message(routeType,msgType,msgContent,msgInfo,extraObjs,
	timeTamp,delayTime)

## 6.相关其他类或者标准
### MessageManager
### MessageRoute
