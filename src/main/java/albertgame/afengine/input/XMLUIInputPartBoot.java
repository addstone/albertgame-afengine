package albertgame.afengine.input;

import albertgame.afengine.app.AppBoot.IXMLPartBoot;
import albertgame.afengine.app.message.Message.IHandler;
import albertgame.afengine.input.ui.UIControlHelp;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.XmlUtil;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 *
 * @author Albert Flex
 */
public class XMLUIInputPartBoot implements IXMLPartBoot{

    /*
        <UIInputPart>
            <InputServlets>
                <before>
                    <name handler="" types=""/>
                    <name handler="" types=""/>
                    ...
                </before>
                <after>
                    <name class="" />    
                </after>
            </InputServlets>
            <UIFaces>
                <name path="" active=""/>
                 ...
            </UIFaces>
        </UIInputPart>
    */
    @Override
    public void bootPart(Element element){
        Element servlets=element.element("InputServlets");
        if(servlets!=null){
            Element bef=servlets.element("before");
            if(bef!=null){
                Iterator<Element> biter=bef.elementIterator();
                while(biter.hasNext()){
                    Element ele=biter.next();
                    List<InputServlet> ser=createServlet(ele);
                    if(ser!=null){
                        ser.forEach((servlet)->{
                            InputManager.getInstance().addPreServlet(servlet.getHandleType(),servlet);                        
                        });
                    }
                }
            }
            Element aft=servlets.element("after");
            if(aft!=null){
                Iterator<Element> biter=aft.elementIterator();
                while(biter.hasNext()){
                    Element ele=biter.next();
                    List<InputServlet> ser=createServlet(ele);
                    if(ser!=null){
                        ser.forEach((servlet)->{
                            InputManager.getInstance().addAfterServlet(servlet.getHandleType(),servlet);                        
                        });
                    }
                }                
            }
        }
        Element faces=element.element("UIFaces");
        if(faces!=null){
            Iterator<Element> faceiter=faces.elementIterator();
            while(faceiter.hasNext()){
                Element ele=faceiter.next();
                createFace(ele,InputManager.getInstance());
            }
        }
    }    
    /*
         <name handler="" types=""/>    
    */
    public static List<InputServlet> createServlet(Element element){
        List<InputServlet> servletList=new LinkedList<>();
        IHandler handler=(IHandler)(FactoryUtil.get().create(element.attributeValue("handler")));
        String name=element.getName();
        if(handler!=null){
            String typess=element.attributeValue("types");
            if(typess==null)return null;
            String[] types=typess.split(",");
            for(String type: types){
                Long ty=Long.parseLong(type);
                InputServlet servlet=new InputServlet(ty,name,handler);
                servletList.add(servlet);
                DebugUtil.log("create servlet:"+servlet.servletName+"-"+servlet.getHandleType());
            }
        }
        return servletList;
    }
    /*
         <name path="" active="true"/>    
    */    
    public static UIFace createFace(Element element,InputManager center){
        String facename=element.getName();
        String path=element.attributeValue("path");
        Document doc=XmlUtil.readXMLFileDocument(path,false);
        if(doc!=null){
            Element root=doc.getRootElement();
            UIFace face=new UIFace(facename);
            face=UIControlHelp.loadFace(face, root);
            String active=element.attributeValue("active");
            if(center!=null)
                center.addFaceInAll(face);
            if(active!=null&&active.equals("true")&&center!=null){
                center.addFaceInActived(face);
            }
            DebugUtil.log("create face:"+face.getFaceName());            
            return face;
        }
        return null;
    }
}
