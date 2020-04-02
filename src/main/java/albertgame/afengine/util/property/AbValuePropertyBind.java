/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util.property;

/**
 *
 * @author albert-flex
 * @param <T> value Type
 */
public abstract class AbValuePropertyBind<T> implements AbValueProperty.IChange<T>{
    
    private T value;
    private final AbValueProperty<T>[] binds;

    public AbValuePropertyBind(AbValueProperty<T> ... binds) {
        this.binds = binds;
        for(AbValueProperty property:binds){
            property.getValueListeners().add(this);
        }
        value=calcValue(value,binds);
    }
    
    public T get(){
        return value;
    }
    
    @Override
    public void Change(T oldValue, T newValue){
        value=calcValue(value,binds);
    }
        
    public abstract T calcValue(T oldvalue,AbValueProperty<T>[] bindproperty);
}
