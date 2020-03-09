## 1.本类、接口、枚举名称
# MessageManager 消息管理器

## 2.本类设计目的
用于消息路由器的管理和消息的发送，将消息具体分发到指定的消息路由器。

## 3.本类的职责概要
管理消息路由器和消息的发送。

## 4.本类的使用方式和生命周期
### 使用方式
创建一个消息路由器的实例，并且加入消息管理器，在将该路由器处理的消息推入消息队列之后，消息管理器会调用消息路由器分发该消息，并且返回消息处理的成功与否。
```
	MessageRoute route1=new MessageRoute1();//假设MessageRoute1是实现了接口的类
	
	MessageManager manager;//获取消息管理器

	manager.addRoute(route1);

	Message msg=new Message(...);//创建一个与route1相同路由类型的消息并且加入到消息管理器
	manager.pushMessage(msg);

	//当消息管理器调用updateQueue更新队列的时候，会自动派送消息，并且将消息送入消息路由器中，由消息路由器决定如何分发，或者不处理	
```
### 生命周期
创建实例之后，将路由器加入到消息管理器后，由消息管理器管理其生命周期，当获取到一个与此路由器转发路由的类型相同的消息之后，便将消息交由此路由器处理。

## 5.本类需要实现的标准api说明
        
        bool addRoute(route) - 添加消息路由器
        MessageRoute getRoute(routeType) - 获取指定消息路由类型的路由器
        bool removeRoute(routeType) - 移除指定消息路由类型的路由器
        pushMessage(msg)    - 将消息推入到消息队列，等待发送
        updateQueue()       - 更新消息队列，并且发送消息

## 6.相关其他类或者标准
### Message
### MessageManager