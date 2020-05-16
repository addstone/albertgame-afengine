/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.message;

/**
 *
 * @author albert-flex
 */
public class Message{
    
    public static interface IRoute{
        long getRouteType();
        void routeMessage(Message msg);
    }
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
