/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app.message;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Albert Flex
 */
public class MessageManager {

    private final Map<Long, Message.IRoute> routeMap;
    private final List<Message.IRoute> waitforAdd;
    private final List<Message.IRoute> waitforRemove;

    private final Deque<Message> messageQueue;
    private int sendmsgOnce;
    private boolean waitdirty;

    public MessageManager() {
        routeMap = new HashMap<>();
        MessageHandlerRoute route=new MessageHandlerRoute();
        routeMap.put(route.getRouteType(),route);
        waitforAdd = new ArrayList<>();
        waitforRemove = new ArrayList<>();
        messageQueue = new ArrayDeque<>();
        sendmsgOnce = 200;
        waitdirty = false;
    }

    public void addRoute(Message.IRoute route) {
        if (routeMap.containsKey(route.getRouteType())) {
            System.out.println("add route failed..already has this route type.");
        } else {
            waitdirty = true;
            waitforAdd.add(route);
        }
    }

    public Message.IRoute getRoute(long type) {
        return routeMap.get(type);
    }

    public void removeRoute(long type) {
        if (!routeMap.containsKey(type)) {
            System.out.println("add route failed..cause no this route type.");
        } else {
            waitdirty = true;
            waitforRemove.add(routeMap.get(type));
        }
    }

    public void updateRoute() {
        if (waitdirty) {
            Iterator<Message.IRoute> routeiter = waitforRemove.iterator();
            while (routeiter.hasNext()) {
                Message.IRoute route = routeiter.next();
                long type = route.getRouteType();;
                routeMap.remove(type);
            }

            routeiter = waitforAdd.iterator();
            while (routeiter.hasNext()) {
                Message.IRoute route = routeiter.next();
                routeMap.put(route.getRouteType(), route);
            }

            waitdirty = false;
        }
    }

    public void pushMessage(Message message) {
        long type = message.routeType;
        if (!routeMap.containsKey(type)) {
            System.out.println("There is no route type for this message");
        } else {
            messageQueue.addFirst(message);
        }
    }

    public Map<Long, Message.IRoute> getRouteMap() {
        return routeMap;
    }

    public int getSendmsgOnce() {
        return sendmsgOnce;
    }

    public void setSendmsgOnce(int sendmsgOnce) {
        this.sendmsgOnce = sendmsgOnce;
    }

    private final Deque<Message> msgStill = new ArrayDeque<>();
    private final Deque<Message> msgShouldSend = new ArrayDeque<>();

    public void updateSendMessage(long time) {
        updateRoute();
        
        long systime = System.currentTimeMillis();

        if (sendmsgOnce == 0) {
            System.out.println("Send once is set 0, is it right?");
            return;
        }
        //一次全部发送可发送消息        
        if (sendmsgOnce < 0) {
            while (!messageQueue.isEmpty()) {
                Message msg = messageQueue.pollLast();
                if ((msg.timetamp + msg.delaytime) <= systime) {
                    msgShouldSend.addFirst(msg);
                } else {
                    msgStill.addFirst(msg);
                }
            }
        } else {
            //每一次按指定次数发送消息
            int count = 0;
            while (!messageQueue.isEmpty() && count < sendmsgOnce) {
                Message msg = messageQueue.pollLast();
                if ((msg.timetamp + msg.delaytime) <= systime) {
                    msgShouldSend.addFirst(msg);
                } else {
                    msgStill.addFirst(msg);
                }
                ++count;
            }
        }

        while (msgStill.isEmpty() == false) {
            Message msg = msgStill.pollLast();
            messageQueue.addLast(msg);
        }
        msgStill.clear();

        while (msgShouldSend.isEmpty() == false) {
            Message msg = msgShouldSend.pollLast();
            Message.IRoute route = routeMap.get(msg.routeType);
            if (route != null) {
                route.routeMessage(msg);
            }
        }
        msgShouldSend.clear();
    }
}
