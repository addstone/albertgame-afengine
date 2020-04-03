/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics.impl;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 *
 * @author Albert Flex
 */
class NullRepaint extends RepaintManager {

    public static void install() {
        RepaintManager repaint = new NullRepaint();
        repaint.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaint);
    }

    public void addInvalidComponent(JComponent c) {
        // do nothing
    }

    public void addDirtyRegion(JComponent c, int x, int y,
            int w, int h) {
        // do nothing
    }

    public void markCompletelyDirty(JComponent c) {
        // do nothing
    }

    public void paintDirtyRegions() {
        // do nothing
    }
}
