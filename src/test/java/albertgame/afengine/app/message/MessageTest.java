/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app.message;

import org.testng.annotations.Test;

/**
 *
 * @author Albert Flex
 */
public class MessageTest {

    @Test
    public void testHandler() {
        Message.IHandler handler = (msg) -> {
            System.out.println(msg.msgInfo);
            return true;
        };
        MessageManager manager = MessageManager.getInstance();
        MessageHandlerRoute route = (MessageHandlerRoute) manager.getRoute(MessageHandlerRoute.Route_Handler);
        route.addTypeContentHandler(1, 1, handler);
        
        Message msg = new Message(MessageHandlerRoute.Route_Handler, 1, 1, "msg Info");
        
        for (int i = 0; i != 10; ++i) {
            manager.pushMessage(msg);
        }        
        manager.updateSendMessage(10);

        for (int i = 0; i != 10; ++i) {
            manager.pushMessage(msg);
        }
        manager.removeRoute(route.getRouteType());
        manager.updateSendMessage(10);
    }
    
    @Test
    public void testRoute(){
        MessageManager manager = MessageManager.getInstance();
        Message.IRoute route =new Message.IRoute(){
            @Override
            public long getRouteType() {
                return 10;
            }
            @Override
            public void routeMessage(Message msg) {
                System.out.println("self route:"+msg.msgInfo);
            }
        };
        manager.addRoute(route);
        manager.updateRoute();

        Message msg=new Message(10,0,0,"self msg");
        manager.pushMessage(msg);
        manager.updateSendMessage(10);
    }
}
