## 1.本类、接口、枚举名称
# ProcessManager 进程管理器类  

## 2.本类设计目的
管理一系列进程的运行。

## 3.本类的职责概要
提供进程类的管理。可以统一管理一系列进程的运行。

## 4.本类的使用方式和生命周期
### 使用方式
当创建进程管理器实例之后，可以将进程加入到管理器，加入时便会初始化进程，当进程管理器更新时，会遍历并且更新全部的进程。当进程的状态为READY_REMOVE时，便会自动移除该进程。
```
	ProcessManager manager=new ProcessManager();//实例化进程管理器
	Process process=new Process1();//假设Process1为一个Process进程类的实现类
	
	manager.attachProcess(process);//将进程实例填入管理器

	Process get=manager.fetchProcess(process.getId());//从管理器中获取指定id的进程

	//操作process
```

### 生命周期
创建一个进程管理器实例之后，便可自由管理实例。如果需要更新填入的进程，则调用updateAllProcess，当需要终止全部正在运行的进程时调用abortAllProcess,会终止全部的进程

## 5.本类需要实现的标准api说明
	
	bool	attachProcess(process)	- 填入进程，成功则返回true，如果进程id已存在，则不填入，并且返回false
	Process fetchProcess(processid)	- 获取指定进程id的进程,如果没有则返回null

	bool 	updateAllProcess(delttime)	-	更新全部的进程们
	void	abortAllProcess()	- 关闭全部的进程，调用进程的abortProcess

## 6.相关其他类或者标准
### Process