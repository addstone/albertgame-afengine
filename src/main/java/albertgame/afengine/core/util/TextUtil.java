/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Albert Flex
 */
public class TextUtil{
    static TextUtil util;
    public static TextUtil get(){
        if(util==null)
            util=new TextUtil();
        return util;
    }
    
    private Properties allTexts;
    private Map<String,URL> langconfigs;
    private String lang;

    private TextUtil() {
        allTexts=new Properties();
        langconfigs=new HashMap<>();
    }
        
    public void addText(String id,String word){
        allTexts.put(id, word);
    }
    
    public String getText(String id){
        return allTexts.getProperty(id);
    }

    public String getLang() {
        return lang;
    }

    public Properties getAllTexts(){
        return allTexts;
    }

    public static String getRealValue(String name){
        if(name.startsWith("@"))
          return getRealValue(name,null);
        else return name;
    }

    public static final String getRealValue(String value,Map<String,String> actorvalues){
        if(value.startsWith("#")){
            String result=actorvalues.get(value.substring(1,value.length()));
            if(result==null)
                result="NullText";
            DebugUtil.log("[#]"+value+" - to - "+result);
            return getRealValue(result,actorvalues);
        }else if(value.startsWith("@")){
            String result=TextUtil.get().getText(value.substring(1,value.length()));
            if(result==null){
                result="NullText";
            }
            DebugUtil.log("[@]"+value+" - to - "+result);
            return getRealValue(result,actorvalues);
        }else return value;
    }    

    public void addLangConfig(String lang,URL url){
        langconfigs.put(lang,url);
    }
    public void setLang(String lang){
        if(this.lang==lang)return;
        loadLang(lang);
        this.lang=lang;
    }
    
    /**
     * <langs default="">
     *    <lang name="" path=""/>
     * </langs>

     * id=value
     * ...
     * @param lang 
     */
    private void loadLang(String lang){
       URL langconfig=langconfigs.get(lang);
       if(langconfig!=null){
           allTexts.clear();
           loadTexts(langconfig);
       }
    }
    private void loadTexts(URL langconfig){
        File file = null;
        try {
            file = new File(langconfig.toURI());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        if(file!=null&&file.isDirectory()){
           loadProperty(file); 
        }
    }
    private void loadProperty(File file){
        if(file.isDirectory()){
            File[] files=file.listFiles();
            for(File f:files){
                loadProperty(f);
            }
        }else{
            Properties p=new Properties();
            try {
                InputStream in = new BufferedInputStream(new FileInputStream(file));
                p.load(in);
                allTexts.putAll(p);
            } catch (Exception ex) {
                DebugUtil.error("file not found:"+file.getPath());
            }
        }
    }
}
