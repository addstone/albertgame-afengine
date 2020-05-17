/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.graphics;

import albertgame.afengine.core.app.App;
import albertgame.afengine.core.app.WindowApp;

/**
 * The GraphicsTech will contains all.
 * Inner implementations is GraphicsTech_Java2DImpl,and GraphicsTech_Java2dImpl2
 * the former can performance well on Window OS,and the after can performance well on Linux OS(ubuntu tested only).
 * @author Administrator
 */
public interface IGraphicsTech extends IGraphicsWindow,IGraphicsState,
        IGraphicsCreate,IGraphicsDraw{
}
