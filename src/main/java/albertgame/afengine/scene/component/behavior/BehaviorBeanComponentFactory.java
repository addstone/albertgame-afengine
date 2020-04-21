package albertgame.afengine.scene.component.behavior;

import albertgame.afengine.scene.ActorComponent;
import albertgame.afengine.scene.IComponentFactory;
import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.FactoryUtil;
import albertgame.afengine.util.TextUtil;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Element;

/**
 * The factory for behaviorbeancomponent.<br>
 * @see IComponentFactory
 * @see BehaviorBeanComponent
 * @author Albert Flex
 */
public class BehaviorBeanComponentFactory implements IComponentFactory{
    
    @Override
    public ActorComponent createComponent(Element element,Map<String,String> datas) {
        /**
         * <BehaviorBean>
         *      <BehaviorName1 class="">
         *          <key value="value"/>
         *          ...
         *      </BehaviorName1>
         *      <BehaviorName2 class="">
         *          ...
         *      </BehaviorName2>
         *      ...
         * </BehaviorBean>
         */
        BehaviorBeanComponent comp = new BehaviorBeanComponent();
        Iterator<Element> valueiter = element.elementIterator();
        while(valueiter.hasNext()){
            Element ele = valueiter.next();
            ActorBehavior behavior = createBehavior(ele,datas);            
            if(behavior!=null){
                comp.addBehavior(behavior);
            }
        }                
                
        return comp;
    }    
    private ActorBehavior createBehavior(Element element,Map<String,String> datas){
        Element be = element;

        String clsname = be.attributeValue("class");
        clsname=TextUtil.getRealValue(clsname,datas);
        String name = be.getName();        

        try {
            ActorBehavior behavior = (ActorBehavior)FactoryUtil.get().create(clsname);
            if(behavior==null){
                DebugUtil.log("class for behavior not correct!");
                return null;
            }
            
            Iterator<Element> valueiter = element.elementIterator();
            while(valueiter.hasNext()){
                Element ele = valueiter.next();
                String key = ele.getName();
                String value = TextUtil.getRealValue(ele.attributeValue("value"),datas);
                behavior.attributes.put(key,value);
            }
            
            behavior.setName(name);
            return behavior;
        } catch (Exception ex) {
            ex.printStackTrace();
            DebugUtil.log("behavior create for - "+element.getName()+" failed. please check again.");
            return null;
        }                
    }
}
