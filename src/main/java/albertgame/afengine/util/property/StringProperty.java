/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util.property;

import albertgame.afengine.util.TextUtil;

/**
 *
 * @author albert-flex
 */
public class StringProperty extends ValueProperty<String>{

    public StringProperty(String value) {
        super(value);
    }

    @Override
    public String get(){
        String key=super.get();        
        return TextUtil.getRealValue(key);
    }        
}
