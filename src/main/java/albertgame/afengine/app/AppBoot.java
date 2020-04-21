/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app;

import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.XmlUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Element;

/**
 *
 * @author Administrator
 */
public class AppBoot {

    /**
     * provide a entry point for XML read for boot app.<br>
     * contains some initial statements for the apptype relatived config.<br>
     * create a AbApp on the bootApp methods, and return it,<br>
     * you could use XMLEngineBoot to boot app xml config file.<br>
     * and you get a element of xml app relatived config.<br>
     * use it Impl class path to xml config, such as following.<br>
     * <app>
        * <afengine typeboot="test.test1.AppTypeBoot1 or boot,boot1" type="type1" 默认为window/>
        * <type1 attri1="" attri2="">
          * <key1>values</key1>
          * <key1>values</key1>
          * ...some nodes.
        * </type1>
        * <settings>
        *   <setting key="" value=""/>
        *   <setting key="" value=""/>
        *   ...
        * </settings>
     * </app>
     *
     * @see App
     * @see XMLEngineBoot
     * @author Albert Flex
     */
    interface IXMLAppTypeBoot {

        public App bootApp(Element element);
    }

    /**
     * provide a entry point for XML boot, used by hand or XMLEngineBoot.<br>
     * contains some part relatived initial statements,and used for xml
     * config<br>
     * create a partsupport in bootPart ,and config this part based by
     * element.<br>
     * unless you make sure that you have no nessesary to return a support,you
     * could return null<br>
     *
     * if you use XMLEngineBoot, remember config the xmlpartboot class to
     * xmlconfig<br>
     * as following<br>
     * <app>
        * <afengine />
        * <apptype />
        * <partsboot>
            * <part name="PartName1" path="test.test1.XMLPartBoot1"/>
            * <part name="PartName2" path="test.test1.XMLPartBoot2"/>
        * </partsboot>
        * <partsconfig>
            * <PartName1 />
            * <PartName2 />
            * ...
        * </partsconfig>
     * </app>
     *
     * @see XMLEngineBoot
     * @see AbPartSupport
     * @author Albert Flex
     */
    public interface IXMLPartBoot {

        /**
         * <PartName>
         * ......
         * </PartName> @param element
         */
        public void bootPart(Element element);
    }

    /**
     * <app>
        * <afengine debug="" logicpath="" typeboot="" type=""/>
        * window <window title="" icon="" size="800,600" render=""/>
        * or window <window title="" icon="" full="true" render=""/>
        * or service <service name=""/>
        * <partsboot>
            * <part name="" path=""/>
            * <part name="" path=""/>
            * <part name="" path=""/>
            * <part name="" path=""/>
        * </partsboot>
        * <partsconfig>
            * <PartName/>
            * <PartName/>
            * <PartName/>
            * ...
        * </partsconfig>
        * <settings>
        *   <setting key="" value=""/>
        *   <setting key="" value=""/>
        *   ...
        * </settings>
     * </app>
     */
    public static void bootEngineFromXML(Element element) {
        App app = null;
        Map<String, IXMLPartBoot> bootMap = new HashMap<>();
        IXMLAppTypeBoot appboot = null;

        Element afe = element.element("afengine");

        String debugs = afe.attributeValue("debug");
        if (debugs != null && debugs.equals("true")) {
            DebugUtil.switchOn();
        }

        String typeboot = afe.attributeValue("typeboot");
        if (typeboot == null) {
            DebugUtil.log("typeboot error,auto to window app boot");
            appboot = new WindowApp.WindowAppBoot();
        } else {
            System.out.println("typeboot:" + typeboot);
            appboot = (IXMLAppTypeBoot) FactoryUtil.get().create(typeboot);
        }
        String type = afe.attributeValue("type");
        if (type == null) {
            type = WindowApp.APPTYPE;
        }

        Element typee = element.element(type);
        if (typee == null) {
            DebugUtil.log(DebugUtil.LogType.SEVER, "app type is not found,auto set to service");
        } else {
            app = appboot.bootApp(typee);
        }

        if (app == null) {
            System.out.println("app create error!");
            return;
        }

        DebugUtil.log("boot app type:" + app.getAppType());
        DebugUtil.log("set app name:" + app.getAppName());

        //put part boot
        DebugUtil.log("-put part boot-");
        Element partlist = element.element("partsboot");
        Iterator<Element> eleiter = null;
        if (partlist != null) {
            eleiter = partlist.elementIterator();
            while (eleiter.hasNext()){
                Element ele = eleiter.next();
                String path = ele.attributeValue("path");
                String bootname = ele.attributeValue("name");
                IXMLPartBoot boot = (IXMLPartBoot) FactoryUtil.get().create(path);
                bootMap.put(bootname, boot);
            }
        }
        //part boot
        DebugUtil.log("-start part config-");
        Element partconfigs = element.element("partsconfig");
        if (partconfigs != null) {
            eleiter = partconfigs.elementIterator();
            while (eleiter.hasNext()) {
                Element ele = eleiter.next();
                String ename = ele.getName();
                IXMLPartBoot boot = bootMap.get(ename);
                if (boot == null) {
                    DebugUtil.log("no boot for:" + ename);
                    continue;
                }
                DebugUtil.log("boot part - " + ename);
            }
        }
        
        DebugUtil.log("-start settings-");
        Element settings=element.element("settings");
        if(settings!=null){
            eleiter=settings.elementIterator();
            while (eleiter.hasNext()) {
                Element ele = eleiter.next();
                String ekey = ele.attributeValue("key");
                String evalue=ele.attributeValue("value");
                app.getSettings().put(ekey, evalue);
                DebugUtil.log("add setting - " + ekey);
            }
        }
        
        //create logic and run.
        String logicpath = afe.attributeValue("logicpath");
        IAppLogic logic = null;
        if (logicpath != null) {
            logic = (IAppLogic) FactoryUtil.get().create(logicpath);
        }
        if (logic != null) {
            app.setLogic(logic);
        }
        
        DebugUtil.log("launch app");
        App.launch(app);
    }

    public static void bootEngine(String xmlpath) {
        Element root = XmlUtil.readXMLFileDocument(xmlpath, false).getRootElement();
        if (root != null) {
            bootEngineFromXML(root);
        }
    }
}
