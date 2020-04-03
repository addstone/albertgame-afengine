/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics.impl;

import albertgame.afengine.graphics.IColor;
import java.awt.Color;

class ColorImpl implements IColor {

    private final Color color;

    public ColorImpl(int r, int g, int b, int a) {
        color = new Color(r, g, b, a);
    }

    public ColorImpl(IColor.GeneraColor colortype) {
        switch (colortype) {
            /*
        RED,GREEN,BLUE,
        YELLOW,GREY,ORANGE,
        WHITE,BLACK,PINK,
                
             */
            case RED:
                color = Color.RED;
                break;
            case GREEN:
                color = Color.GREEN;
                break;
            case BLUE:
                color = Color.BLUE;
                break;
            case YELLOW:
                color = Color.YELLOW;
                break;
            case GRAY:
                color = Color.GRAY;
                break;
            case ORANGE:
                color = Color.ORANGE;
                break;
            case WHITE:
                color = Color.WHITE;
                break;
            case BLACK:
                color = Color.BLACK;
                break;
            case PINK:
                color = Color.PINK;
                break;
            default:
                color = Color.CYAN;
                break;
        }
    }

    public Color getColor() {
        return color;
    }

    @Override
    public int getRed() {
        return color.getRed();
    }

    @Override
    public int getGreen() {
        return color.getGreen();
    }

    @Override
    public int getBlue() {
        return color.getBlue();
    }

    @Override
    public int getAlpha() {
        return color.getAlpha();
    }

    @Override
    public IColor getAntiColor() {
        int r = 255 - color.getRed();
        int g = 255 - color.getGreen();
        int b = 255 - color.getBlue();
        return new ColorImpl(r, g, b, this.getAlpha());
    }
}
