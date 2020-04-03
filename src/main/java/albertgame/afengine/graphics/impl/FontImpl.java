/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics.impl;

import albertgame.afengine.graphics.IFont;
import albertgame.afengine.graphics.IFont.FontStyle;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Albert Flex
 */
class FontImpl implements IFont {

    private Font font;
    private FontStyle style;

    public FontImpl(String fontValue, boolean isPath, FontStyle style, int size) {
        this.style = style;
        int st = Font.PLAIN;
        if (style == FontStyle.BOLD) {
            st = Font.BOLD;
        } else if (style == FontStyle.ITALIC) {
            st = Font.ITALIC;
        }
        if (isPath) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, new File(fontValue));
                font = font.deriveFont(st, size);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
        } else {
            font = new Font(fontValue, st, size);
        }
    }

    public FontImpl(URL url, FontStyle style, int size) {
        this.style = style;
        int st = Font.PLAIN;
        if (style == FontStyle.BOLD) {
            st = Font.BOLD;
        } else if (style == FontStyle.ITALIC) {
            st = Font.ITALIC;
        }
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(url.toURI()));
            font = font.deriveFont(st, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFontName() {
        return font.getFontName();
    }

    @Override
    public FontStyle getFontStyle() {
        return style;
    }

    @Override
    public int getFontHeight() {
        return font.getSize();
    }

    public Font getFont() {
        return font;
    }

    @Override
    public int getFontWidth(String text) {
        FontMetrics trics = Toolkit.getDefaultToolkit().getFontMetrics(this.font);
        return trics.stringWidth(text);
    }
}
