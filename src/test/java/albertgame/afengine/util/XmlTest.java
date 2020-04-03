/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import java.net.URL;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.testng.annotations.Test;

/**
 *
 * @author Albert Flex
 */
public class XmlTest {
    
    @Test
    public void loadXml(){
        URL url=getClass().getClassLoader().getResource("xml1.xml");
        Document doc=XmlUtil.readXMLFileDocument(url,true);
        Element root=doc.getRootElement();
        Iterator<Element> eiter=root.elementIterator();
        while(eiter.hasNext()){
            Element ele=eiter.next();
            System.out.println(ele.getName()+">"+ele.getText());
        }
    }
    
    @Test
    public void outXMl(){
        URL url=getClass().getClassLoader().getResource("xml2.xml");
        System.out.println(url.toString());
        Document doc=XmlUtil.readXMLFileDocument(url,true);
        Element root=doc.getRootElement();
        Iterator<Element> eiter=root.elementIterator();
        while(eiter.hasNext()){
            Element ele=eiter.next();
            System.out.println(ele.getName()+">"+ele.getText());
        }        
        root.clearContent();
        Element ele=root.addElement("groupId");
        ele.setText("albertgame");
        Element ele2=root.addElement("artifactId");
        ele2.setText("afengine");
        XmlUtil.writeXMLFile(url, doc);
    }
}
