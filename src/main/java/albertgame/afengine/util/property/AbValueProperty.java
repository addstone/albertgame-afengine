/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util.property;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author albert-flex
 * @param <T> value type.
 */
public abstract class AbValueProperty<T> {

    public static interface IChange<T> {

        void Change(T oldValue, T newValue);
    }

    private T value;
    private final List<IChange<T>> valueListeners;

    public AbValueProperty(T value) {
        this.value = value;
        valueListeners = new ArrayList<>();
    }

    public T get() {
        synchronized (this) {
            return value;
        }
    }

    public void set(T newvalue) {
        synchronized (this) {
            //通知所有的属性改变监听器
            T oldvalue=value;
            value = newvalue;
            valueListeners.forEach((IChange change) -> {
                change.Change(oldvalue, newvalue);
            });
        }
    }

    public List<IChange<T>> getValueListeners() {
        return valueListeners;
    }
}
