/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author Albert Flex
 */
public class XmlUtil {

    private static final Logger log = Logger.getLogger(XmlUtil.class.getName());
        
    public static Document getXMLWritableDocument(String xmlpath){
        if(xmlpath==null){
            Document doc=DocumentHelper.createDocument();
            doc.addElement("root");
            return doc;
        }

        File file = new File(xmlpath);
        if(!file.exists()){
            Document doc=DocumentHelper.createDocument();
            doc.addElement("root");
            return doc;            
        }
        SAXReader reader =new SAXReader();
        Document doc;
        try{
            doc = reader.read(file);            
            log.log(Level.INFO,"root : "+doc.getRootElement().getName());
        }catch(DocumentException e){
            e.printStackTrace();
            return null;
        }
        return doc;        
    }
    public static Document readXMLFileDocument(String xmlpath){
        if(xmlpath==null){
            log.log(Level.INFO,"read xml can not null!");
            return null;
        }

        File file = new File(xmlpath);
        if(!file.exists()){
            log.log(Level.INFO,"the xml file - "+xmlpath+" is not exist.");
            return null;
        }
        SAXReader reader =new SAXReader();
        Document doc;
        try{
            doc = reader.read(file);            
            log.log(Level.INFO,"root : "+doc.getRootElement().getName());
        }catch(DocumentException e){
            e.printStackTrace();
            return null;
        }
        return doc;
    }
    public static void writeXMLFile(String xmlpath,Document doc){
        FileOutputStream out =null;
        try {
            out = new FileOutputStream(xmlpath);
            OutputFormat format=OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
            format.setEncoding("UTF-8");
            XMLWriter writer=new XMLWriter(out,format);
            //2.写出Document对象
            writer.write(doc);
            //3.关闭流
            writer.close();
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }
}
