/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.util.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author albert-flex
 * @param <T> value Type
 */
public abstract class AbValuePropertyBind<T> implements ValueProperty.IChange<T>{
    
    private T value;
    private final List<ValueProperty<T>> binds;

    public AbValuePropertyBind(ValueProperty<T> ... binds) {
        this.binds = new ArrayList<>();
        for(ValueProperty property:binds){
            property.getValueListeners().add(this);
        }
        value=calcValue(value,Arrays.asList(binds));
    }
    
    public T get(){
        return value;
    }
    
    @Override
    public void Change(T oldValue, T newValue){
        value=calcValue(value,binds);
    }
        
    public abstract T calcValue(T oldvalue,List<ValueProperty<T>> bindproperty);        
}
