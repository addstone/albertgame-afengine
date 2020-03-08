# 对象名称
## ValueProperty<T>属性值

# 对象介绍
## 属性值旨在提供可见听值改变的属性。

# 对象功能

## 获取/设置属性值
## 获取/设置数值改变监听器

# 对象相关类
## ValueChangeListener<T> 属性更改监听器
### 对象功能
#### boolean change(T oldv,T newv);如果返回false，表示更改失败，否则会将属性更改成新属性

#### 内部实现类
	LongProperty,
	IntProperty,
	DoubleProperty,
	StringProperty,