## 1.本类、接口、枚举名称
# Process 进程类[抽象]  

## 2.本类设计目的
为可以自由更新或者可以长时间存在的行为提供支持。并且进程以进程链为组成成分，可以创建链结构的进程链，以供方便以流形式运行进程。

## 3.本类的职责概要
提供进程的知识。外与进程管理器，或者自定义的代码交互，内部与只有自己了解的内容交互，更新自己的内容。以链形式组织。提供通用的可行的逻辑控制。

## 4.本类的使用方式和生命周期
### 使用方式
创建一个进程以供应用运行或者管理自定义的部分，用户需要由适当的时期调用进程类的生命周期函数以实现进程控制。可以使用进程管理器ProcessManager来管理进程的生命周期。
```
	Process process=new Process1();//假设Process1是实现了的Process进程，创建进程实例

	ProcessManager manager;
	...//获取进程管理器
	manager.attachProcess(process);//将进程加入到进程管理器

	...//或者可以自行管理器生命周期
	process.initProcess();//初始化进程
	process.updateProcess(delttime);//在适当时机调用更新进程
	
	//process.successProcess();//成功结束进程，会回调successAct()方法

	//process.failProcess();//进程运行失败导致结束，会回调failedAct()方法

	//process.abortProcess();//中止进程，会回调abortAct()方法

```
### 生命周期
生命周期由进程管理器(将进程实例加入到进程管理器时)管理，或者可以自行管理。
需要自己实现initProcess(),updateProcess(delttime),当进程实例加入到管理器后，便被调用initProcess初始化进程，之后进入管理器更新的时候，会自动被调用updateProcess，当进程在运行当中，调用自身的sucessProcess,failedProcess或者abortProcess方法后，便会进行相应回调，之后便会被进程管理器移除更新的列表，生命周期结束。

## 5.本类需要实现的标准api说明

	Process(id,info)		- 以唯一指定的Id值构建进程，指定进程信息
	long   getId()		- 获取进程的唯一ID值
	string getInfo()	- 获取进程的说明信息
	ProcessState getState() 	- 获取进程状态
	
	bool initProcess() 		- 初始化进程，回调initAct方法,当初始化失败之后，会返回false，并且设置内部状态为
	bool updateProcess(delttime)		- 更新进程，回调updateAct(delttime)	
	void pauseProcess() 		- 暂停进程，会回调pauseAct
	void resumeProcess() 		- 继续进程，会回调resumeAct

	void successProcess()		- 成功结束进程，会回调successAct
	void failProcess() 		- 进程运行失败，会回调failAct
	void abortProcess()			- 进程被中止，会回调abortAct

	void initAct()			- 私有或者保护方法，由进程内initProcess调用，需要实现类覆盖重写
	void updateAct(delttime)			- 私有或者保护方法，由进程内updateProcess调用，需要实现类覆盖重写
	void pauseAct()			- 私有或者保护方法，由进程内pauseProcess调用，需要实现类覆盖重写
	void resumeAct()			- 私有或者保护方法，由进程内resumeProcess调用，需要实现类覆盖重写
	void sucessAct()			- 私有或者保护方法，由进程内successProcess调用，需要实现类覆盖重写
	void failAct()			- 私有或者保护方法，由进程内failedProcess调用，需要实现类覆盖重写
	void abortAct()			- 私有或者保护方法，由进程内abortProcess调用，需要实现类覆盖重写
	
	Process getNextProcess()	- 获取后继的进程
	void	setNextProcess(process) - 设置后继的进程，

## 6.相关其他类或者标准
### ProcessManager