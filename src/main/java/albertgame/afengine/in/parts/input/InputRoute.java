/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.in.parts.input;

import albertgame.afengine.core.input.InputServlet;
import albertgame.afengine.core.message.Message;
import albertgame.afengine.core.message.Message.IRoute;
import albertgame.afengine.core.util.math.IDGenerator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class InputRoute implements IRoute {

    public static long Route_Input = IDGenerator.createId();

    private final InputManager center;

    public InputRoute() {
        center = InputManager.getInstance();
    }

    @Override
    public long getRouteType() {
        return Route_Input;
    }

    //先通过先界面过滤列表
    //后通过已激活界面或者弹出界面
    //最后通过后界面过滤列表
    @Override
    public void routeMessage(Message msg) {
        Map<Long, List<InputServlet>> preServlets = center.getPreServlets();
        List<InputServlet> slist = preServlets.get(msg.msgType);
        if (slist != null) {
            for (InputServlet ser : slist) {
                if (ser.handle(msg)) {
                    return;
                }
            }
        }

        UIFace popupFace = center.getPopupFace();
        if (popupFace != null) {
            if (popupFace.handle(msg)) {
                return;
            }
        } else {
            try {
                List<UIFace> activedFaces = center.getActivedfaceList();
                for (UIFace face : activedFaces) {
                    if (face.handle(msg)) {
                        return;
                    }
                }
            } catch (Exception ex) {
            }
        }

        Map<Long, List<InputServlet>> afterServlets = center.getAfterServlets();
        slist = afterServlets.get(msg.msgType);
        if (slist != null) {
            for (InputServlet ser : slist) {
                if (ser.handle(msg)) {
                    return;
                }
            }
        }
    }
}
