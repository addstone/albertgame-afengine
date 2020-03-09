## 1.本类、接口、枚举名称
# ProcessState 进程状态枚举

## 2.本类设计目的
设计为表示进程的运行状态，值有如下:
UNSETUP,//未启动
RUNNING,//正在运行
PAUSED,//已暂停

EXCEPTION,//异常情况
END_SUCCESS,//进程正常运行结束
END_FAILED,//失败导致进程终结
END_ABORT,//进程被终止运行

READY_REMOVE,//已经清理完毕，可以结束进程的生命

## 3.本类的职责概要
表示进程的运行状态

## 4.本类的使用方式和生命周期
### 使用方式
表示Process的状态，使用set/get设置或者获取进程的状态  

### 生命周期
无

## 5.本类需要实现的标准api说明

## 6.相关其他类或者标准
### Process
### ProcessManager
