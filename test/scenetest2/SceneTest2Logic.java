package scenetest2;

import afengine.core.IAppLogic;
import afengine.core.util.XMLEngineBoot;

public class SceneTest2Logic implements IAppLogic{

    public static void main(String[] args) {
        XMLEngineBoot.bootEngine("test/scenetest2/scenetest-test2-boot.xml");
    }
    @Override
    public boolean init() {
        return true;
    }

    @Override
    public boolean update(long l) {
        return true;
    }

    @Override
    public boolean shutdown() {
        return true;
    }    
}
