/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.graphics.impl;

import albertgame.afengine.graphics.ITexture;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrator
 */
class TextureImpl implements ITexture {

    private Image img;
    
    public TextureImpl(String path) {
        img = new ImageIcon(path).getImage();
    }
    
    public TextureImpl(URL url) {
        img = new ImageIcon(url).getImage();
    }

    public TextureImpl() {
    }

    @Override
    public int getWidth() {
        return img.getWidth(null);
    }

    @Override
    public int getHeight() {
        return img.getHeight(null);
    }

    public Image getImage() {
        return img;
    }

    @Override
    public ITexture scaleInstance(double sx, double sy) {
        TextureImpl texture = new TextureImpl();
        BufferedImage bi = createCompatibleImage(getWidth(), getHeight());
        Graphics2D grph = (Graphics2D) bi.getGraphics();
        grph.scale(sx, sy);
        grph.drawImage(img, 0, 0, null);
        grph.dispose();
        texture.img = bi;
        return texture;
    }

    @Override
    public ITexture cutInstance(int x, int y, int w, int h) {
        TextureImpl texture = new TextureImpl();
        BufferedImage bi = createCompatibleImage(w, h);
        Graphics2D grph = (Graphics2D) bi.getGraphics();
        grph.drawImage(img, 0, 0, w, h, x, y, x + w, y + h, null);
        grph.dispose();
        texture.img = bi;
        return texture;
    }

    private BufferedImage createCompatibleImage(int w, int h) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        return gc.createCompatibleImage(w, h, Transparency.OPAQUE);
    }
}
