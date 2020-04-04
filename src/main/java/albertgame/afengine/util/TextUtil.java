/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import albertgame.afengine.app.App;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Albert Flex
 */
public class TextUtil{
    
    private Map<String,String> allTexts;
    private String lang;

    public TextUtil(String lang) {
        allTexts=new HashMap<>();
    }

    public void addText(String id,String word){
        allTexts.put(id, word);
    }
    
    public String getText(String id){
        return allTexts.get(id);
    }

    public String getLang() {
        return lang;
    }

    public Map<String, String> getAllTexts() {
        return allTexts;
    }

    public static final String getRealValue(String value,Map<String,String> actorvalues){
        if(value.startsWith("#")){
            String result=actorvalues.get(value.substring(1,value.length()));
            if(result==null)
                result="NullText";
            DebugUtil.log(DebugUtil.LogType.INFO,"[#]"+value+" - to - "+result);
            return getRealValue(result,actorvalues);
        }else if(value.startsWith("@")){
            String result=App.textUtil.getText(value.substring(1,value.length()));
            if(result==null){
                result="NullText";
            }
            DebugUtil.log(DebugUtil.LogType.INFO,"[@]"+value+" - to - "+result);
            return getRealValue(result,actorvalues);
        }else return value;
    }    
}
