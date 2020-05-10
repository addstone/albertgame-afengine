/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util.property;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author albert-flex
 * @param <T> value type
 */
public class ValuePropertyList<T> {

    public static interface IChange<T> {

        void add(final List<T> valuelist, T newvalue, int newvalueIndex);

        void remove(final List<T> valuelist, T removeValue, int removeIndex);
    }

    private final List<T> valueList;
    private final List<IChange<T>> changelistener;

    public ValuePropertyList() {
        valueList = new LinkedList<>();
        changelistener = new LinkedList<>();
    }

    public List<T> get() {
        synchronized (this) {
            return valueList;
        }
    }

    public void addValueProperty(T newvalue) {
        synchronized (this) {
            int length = valueList.size();
            changelistener.forEach((IChange change) -> {
                change.add(valueList, newvalue, length);
            });
            valueList.add(newvalue);
        }
    }

    public void removeValueProperty(T oldvalue) {
        synchronized (this) {
            int index = valueList.indexOf(oldvalue);
            if (index == -1) {
                //no remove
                return;
            }
            changelistener.forEach(((IChange change) -> {
                change.remove(valueList, oldvalue, index);
            }));
            valueList.remove(index);
        }
    }
    public List<IChange<T>> getChangelistener() {
        return changelistener;
    }
}
