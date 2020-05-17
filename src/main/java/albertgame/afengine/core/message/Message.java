/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.message;

/**
 * 表示一个可以传输的消息，提供核心部分的消息处理
 * @author albert-flex
 */
public class Message{
    
    /**
     * 提供对消息路由转发的功能
     */
    public static interface IRoute{
        long getRouteType();
        void routeMessage(Message msg);
    }
    /**
     * 提供对消息进行处理的功能
     */
    public static interface IHandler{
        boolean handle(Message msg);
    }
    
    public final long routeType;//路由类型
    
    public final long msgType;//消息类型
    public final long msgContent;//消息内容
    public final String msgInfo;//消息提示
    public final Object[] extraObjs;//消息附加内容
    
    public final long timetamp;//时间戳
    public final long delaytime;//发送延迟时间

    public Message(long routeType, long msgType, long msgContent, String msgInfo, Object[] extraObjs, long timetamp, long delaytime) {
        this.routeType = routeType;
        this.msgType = msgType;
        this.msgContent = msgContent;
        this.msgInfo = msgInfo;
        this.extraObjs = extraObjs;
        this.timetamp = timetamp;
        this.delaytime = delaytime;
    }

    public Message(long routeType, long msgType, long msgContent, String msgInfo, Object[] extraObjs,long delaytime) {
        this(routeType,msgType,msgContent,msgInfo,extraObjs,System.currentTimeMillis(),delaytime);
    }

    public Message(long routeType, long msgType, long msgContent, String msgInfo, Object[] extraObjs) {
        this(routeType,msgType,msgContent,msgInfo,extraObjs,0);
    }
    public Message(long routeType, long msgType, long msgContent, String msgInfo){
        this(routeType,msgType,msgContent,msgInfo,null);
    }
}
