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

        void Change(final List<T> valuelist, T newvalue, int newvalueIndex);
    }

    private final List<T> valueList;
    private final List<IChange> changelistener;

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
                change.Change(valueList, newvalue, length);
            });
            valueList.add(newvalue);
        }
    }

    public List<IChange> getChangelistener() {
        return changelistener;
    }
}
