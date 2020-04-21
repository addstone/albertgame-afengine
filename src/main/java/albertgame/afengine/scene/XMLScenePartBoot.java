/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.scene;

import albertgame.afengine.app.AppBoot.IXMLPartBoot;
import albertgame.afengine.scene.ActorComponent.IProcess;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.XmlUtil;
import albertgame.afengine.scene.Scene.Loader;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * partboot for scene<br>
 *
 * @see IXMLPartBoot
 * @author Albert Flex
 */
public class XMLScenePartBoot implements IXMLPartBoot {

    public static interface IComponentFactoryLoader {

        public IComponentFactory loadFactory(Element element);
    }

    /**
     * <ScenePart main="">
        * <ComponentFactoryList>
            * <Component name="" class="" loader=""/>
            * <Component name="" class=""/>
            * <Component name="" class=""/>
        * </ComponentFactoryList>
        * <SceneList>
            * <Scene id="" name="" class="" or path="" loader="" output="true"/>
            * <Scene id="" name="" class="" or path="" loader=""/>
        * </SceneList>
        * <ComponentMethods>
            * <method class=""/>
            * ...
        * </ComponentMethods>
        * <StaticActors path=""/>
     * </ScenePart>
     *
     * @param element
     * @return
     */
    @Override
    public void bootPart(Element element) {

        String main = element.attributeValue("main");

        Iterator<Element> eleiter = element.element("ComponentFactoryList").elementIterator();
        while (eleiter.hasNext()) {
            Element ele = eleiter.next();
            String name = ele.attributeValue("name");
            String classname = ele.attributeValue("class");
            IComponentFactory fac = (IComponentFactory) FactoryUtil.get().create(classname);
            ActorComponent.addFactory(name, fac);
            DebugUtil.log("Add ComponentFactory :" + name);
            String loader = ele.attributeValue("loader");
            if (loader != null) {
                IComponentFactoryLoader floader = loadfactory(loader);
                floader.loadFactory(ele);
            }
        }

        eleiter = element.element("SceneList").elementIterator();
        while (eleiter.hasNext()) {
            Element ele = eleiter.next();
            String scenename = ele.attributeValue("name");
            Scene scene = loadScene(ele);
            if (scene != null) {
                scene.setName(scenename);
                scene.awakeAllActors();
                DebugUtil.log("Prepare Scene :" + scene.getName());
                SceneManager.getInstance().prepareScene(scene);
            }
        }

        Scene mainscene = SceneManager.getInstance().findPreparedScene(main);
        if (mainscene == null) {
            DebugUtil.log("MainSceneNotFound!");
        } else {
            SceneManager.getInstance().pushScene(mainscene);
            DebugUtil.log("Push Main Scene Successfully");
        }

        //load static actors
        Element staticActorsE = element.element("StaticActorList");
        if (staticActorsE != null) {
            String path = staticActorsE.attributeValue("path");
            if (path != null) {
                Document doc = XmlUtil.readXMLFileDocument(path, false);
                if (doc != null && doc.getRootElement() != null) {
                    Element root = doc.getRootElement();
                    if (root.getName().equals("StaticActorList")) {
                        SceneFileHelp.loadStaticActorFromXML(root);
                    }
                }
            }
        }

        
        //load componentmethods
        /*
        * <ComponentMethods>
            * <method class=""/>
            * ...
        * </ComponentMethods>        
        */
        Element compms=element.element("ComponentMethods");
        if(compms!=null){
            Iterator<Element> eiter=compms.elementIterator();
            while(eiter.hasNext()){
                Element e=eleiter.next();
                String clss=e.attributeValue("class");
                IProcess process=(IProcess) FactoryUtil.get().create(clss);
                if(process!=null){
                    ActorComponent.componentMethodList.add(process);
                }
            }
        }
    }

    private IComponentFactoryLoader loadfactory(String factoryclass) {
        IComponentFactoryLoader loader = (IComponentFactoryLoader) FactoryUtil.get().create(factoryclass);
        return loader;
    }

    private Scene loadScene(Element sceneEle) {

        Scene scene = null;
        String classpath = sceneEle.attributeValue("class");
        String id = sceneEle.attributeValue("id");
        Loader loader = null;
        String loaderc = sceneEle.attributeValue("loader");
        if (loaderc != null) {
            loader = (Loader) FactoryUtil.get().create(loaderc);
        }
        if (classpath != null) {
            scene=(Scene)FactoryUtil.get().create(classpath);
                String name = sceneEle.attributeValue("name");
                scene.setName(name);
        } else {
            //load scene from xml file 
            String path = sceneEle.attributeValue("path");
            if (path != null) {
                Document scenedoc = XmlUtil.readXMLFileDocument(path,false);
                if (scenedoc != null) {
                    Element root = scenedoc.getRootElement();
                    if (root != null && root.getName().equals("scene")) {
                        scene = SceneFileHelp.loadSceneFromXML(root);
                    }
                }
            }
        }

        if (loader != null && scene != null) {
            scene.setLoader(loader);
        }

        return scene;
    }
}
