/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

    private static Document readXML(URL url, String xmlpath, boolean isUrl, boolean create) {
        File file = null;
        try {
            if (isUrl) {
                file = new File(url.toURI());
            } else {
                if (xmlpath == null) {
                    System.out.println("read xml can not null!");
                    return null;
                }
                file = new File(xmlpath);
            }
            if (!file.exists()) {
                Document doc;
                if (create) {
                    doc = DocumentHelper.createDocument();
                    doc.addElement("root");
                } else {
                    doc = null;
                }
                return doc;
            }
            SAXReader reader = new SAXReader();
            Document doc;
            try {
                doc = reader.read(file);
                System.out.println("root : " + doc.getRootElement().getName());
            } catch (DocumentException e) {
                e.printStackTrace();
                return null;
            }
            return doc;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Document readXMLFileDocument(String xmlpath, boolean create) {
        return readXML(null, xmlpath, false, create);
    }

    public static Document readXMLFileDocument(URL url, boolean create) {
        return readXML(url, null, true, create);
    }

    private static void writeXML(URL url, String xmlpath, boolean isUrl, Document doc) {

        FileOutputStream out = null;
        try {
            if (isUrl) {
                out = new FileOutputStream(new File(url.toURI()));
            } else {
                out = new FileOutputStream(new File(xmlpath));
            }
            OutputFormat format = OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            //2.写出Document对象
            writer.write(doc);
            //3.关闭流
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void writeXMLFile(URL url, Document doc) {
        writeXML(url, null, true, doc);
    }

    public static void writeXMLFile(String xmlpath, Document doc) {
        writeXML(null, xmlpath, false, doc);
    }
}
