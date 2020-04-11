/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app.message;

import albertgame.afengine.util.DebugUtil;
import albertgame.afengine.util.DebugUtil.LogType;
import albertgame.afengine.util.math.IDGenerator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Albert Flex
 */
public class MessageHandlerRoute implements Message.IRoute {

    private class MessageHandler {
        public final Map<Long, List<Message.IHandler>> contentHandlerMap;
        public MessageHandler() {
            contentHandlerMap = new HashMap<>();
        }
    }

    private final Map<Long, MessageHandler> typeHandlerMap;
    public static final long Route_Handler=new IDGenerator().createId();

    public MessageHandlerRoute() {
        this.typeHandlerMap = new HashMap<>();
        IDGenerator ge = new IDGenerator();
    }

    public void addTypeContentHandler(long type, long content, Message.IHandler handler) {
        MessageHandler handlers = typeHandlerMap.get(type);
        if (handlers == null) {
            handlers = new MessageHandler();
            typeHandlerMap.put(type, handlers);
        }
        List<Message.IHandler> handlerlist=null;
        if(!handlers.contentHandlerMap.containsKey(content)){
            handlerlist=new LinkedList<>();
            handlers.contentHandlerMap.put(content, handlerlist);
        }else{
            handlerlist=handlers.contentHandlerMap.get(content);
        }
        if (!handlerlist.contains(handler)) {
            handlerlist.add(handler);
        }
    }

    public boolean hasTypeContentHandler(long type, long content, Message.IHandler handler) {
        MessageHandler handlers = typeHandlerMap.get(type);
        if (handlers == null) {
            return false;
        }
        if (handlers.contentHandlerMap.containsKey(content)) {
            List<Message.IHandler> handlerlist = handlers.contentHandlerMap.get(content);
            return handlerlist.contains(handler);
        } else {
            return false;
        }
    }

    public void removeTypeContentHandler(long type, long content, Message.IHandler handler) {
        MessageHandler handlers = typeHandlerMap.get(type);
        if (handlers == null) {
            return;
        }
        if (handlers.contentHandlerMap.containsKey(content)) {
            List<Message.IHandler> handlerlist = handlers.contentHandlerMap.get(content);
            handlerlist.remove(handler);
        }
    }

    public List<Message.IHandler> getTypeContentHandler(long type, long content) {
        MessageHandler handlers = typeHandlerMap.get(type);
        if (handlers == null) {
            return null;
        }
        if (handlers.contentHandlerMap.containsKey(content)) {
            List<Message.IHandler> handlerlist = handlers.contentHandlerMap.get(content);
            return handlerlist;
        }
        return null;
    }

    @Override
    public long getRouteType() {
        return Route_Handler;
    }

    @Override
    public void routeMessage(Message msg) {
        MessageHandler handlers = typeHandlerMap.get(msg.msgType);
        if (handlers == null) {
            DebugUtil.log(LogType.WARNING, "No message bighandler for msgType.");
            return;
        }

        if (handlers.contentHandlerMap.containsKey(msg.msgContent)) {
            List<Message.IHandler> handlerlist = handlers.contentHandlerMap.get(msg.msgContent);
            for (Message.IHandler handler : handlerlist) {
                if (handler.handle(msg)) {
                    return;
                }
            }
        }
    }
}
