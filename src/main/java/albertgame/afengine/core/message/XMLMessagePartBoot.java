/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.message;

import albertgame.afengine.core.app.AppBoot.IXMLPartBoot;
import albertgame.afengine.core.message.Message.IRoute;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.FactoryUtil;
import org.dom4j.Element;
import java.util.Iterator;

/**
 * partboot of messagepart<br>
 * @author Albert Flex
 */
public class XMLMessagePartBoot implements IXMLPartBoot{
    
    /**
     * <MessagePart>
     *      <Route path=""/>
     *      <Route path=""/>
     * </MessagePart>
     * @param element 
     * @return  partsupport
     */
    @Override
    public void bootPart(Element element){
        MessageManager center =MessageManager.getInstance();        
        Iterator<Element> routeiter = element.elementIterator();
        while(routeiter.hasNext()){
            Element ele = routeiter.next();
            String path = ele.attributeValue("path");
            try{
                IRoute route = (IRoute)FactoryUtil.create(path);                
                center.addRoute(route);
                DebugUtil.log("add route ["+path+"]");
            }catch(Exception ex){
                DebugUtil.log("add message route error!");
                continue;
            }                            
        }
    }    
}
