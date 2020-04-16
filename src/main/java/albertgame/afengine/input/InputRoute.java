/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.input;

import albertgame.afengine.app.message.Message;
import albertgame.afengine.app.message.Message.IRoute;
import albertgame.afengine.util.math.IDGenerator;

/**
 *
 * @author Administrator
 */
public class InputRoute implements IRoute {

    public static long Route_Input = new IDGenerator().createId();
    @Override
    public long getRouteType() {
        return Route_Input;
    }

    @Override
    public void routeMessage(Message msg){
        
    }
}
