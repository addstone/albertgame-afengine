/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.in.parts.scene;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.AppBoot.IXMLPartBoot;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.FactoryUtil;
import albertgame.afengine.core.util.XmlUtil;
import albertgame.afengine.in.parts.scene.ActorComponent.IProcess;
import albertgame.afengine.in.parts.scene.Scene.Loader;
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
     * <Component name="" class="" loader="" process="disable"/>
     * <Component name="" class=""/>
     * <Component name="" class=""/>
     * </ComponentFactoryList>
     * <SceneList>
     * <Scene id="" name="" class="" or path="" loader=""/>
     * <Scene id="" name="" class="" or path="" loader=""/>
     * </SceneList>
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
            String loader = ele.attributeValue("loader");
            IComponentFactory fac=null;
            if (loader != null) {
                IComponentFactoryLoader floader = loadfactory(loader);
                fac = floader.loadFactory(ele);
                ActorComponent.addFactory(name, fac);
            }else{
                String classname = ele.attributeValue("class");
                fac = (IComponentFactory) FactoryUtil.create(classname);                
            }
            ActorComponent.addFactory(name, fac);
            DebugUtil.log("Add ComponentFactory :" + name);
            String process = ele.attributeValue("process");
            //没有就代表使用默认
            //如果为disable则为放弃该组件的场景层面的操作,否则使用该字段实例化Process
            if (process == null) {
                ActorComponent.componentMethodList.add(new ActorComponent.AdapterProcess(name));
            } else if (!process.equals("disable")) {
                IProcess processin = (IProcess) FactoryUtil.create(process);
                if (processin != null) {
                    ActorComponent.componentMethodList.add(processin);
                }
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

        //注册场景进程到应用进程中
        App.getInstance().getProcessManager().attachProcess(new SceneManager.SceneProcess());
    }

    private IComponentFactoryLoader loadfactory(String loaderpath) {
        IComponentFactoryLoader loader = (IComponentFactoryLoader) FactoryUtil.create(loaderpath);
        return loader;
    }

    private Scene loadScene(Element sceneEle) {

        Scene scene = null;
        String classpath = sceneEle.attributeValue("class");
        Loader loader = null;
        String loaderc = sceneEle.attributeValue("loader");
        if (loaderc != null) {
            loader = (Loader) FactoryUtil.create(loaderc);
        }
        if (classpath != null) {
            scene = (Scene) FactoryUtil.create(classpath);
            String name = sceneEle.attributeValue("name");
            scene.setName(name);
        } else {
            //load scene from xml file 
            String path = sceneEle.attributeValue("path");
            if (path != null) {
                Document scenedoc = XmlUtil.readXMLFileDocument(path, false);
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
