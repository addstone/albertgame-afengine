/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.app;

/**
 * logic of App, you must implement one ,and setup at App
 * @See  App
 * @author Albert Flex
 */
public interface IAppLogic {

    public boolean init();

    public boolean update(long time);

    public boolean shutdown();

    public static class AppLogicBase implements IAppLogic {

        @Override
        public boolean init() {
            return true;
        }

        @Override
        public boolean update(long time) {
            return true;
        }

        @Override
        public boolean shutdown() {
            return true;
        }
    }
}
