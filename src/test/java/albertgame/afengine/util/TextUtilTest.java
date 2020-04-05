/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class TextUtilTest {

    public static void main(String[] args) {
        new TextUtilTest().testLoad();
    }
    
    public void testLoad() {
        TextUtil util = TextUtil.get();
        URL zh = getClass().getClassLoader().getResource("lang/zh");
        util.setLangConfig("zh", zh);
        util.loadLang("zh");
        print(util.getAllTexts());
    }

    private void print(Properties p) {
        System.out.println("ID\tVALUE");
        Set<Map.Entry<Object, Object>> entry = p.entrySet();
        entry.forEach((en) -> {
            System.out.println(en.getKey().toString()+"\t"+en.getValue().toString());
        });
    }
}
