/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.in.core.graphics;

import albertgame.afengine.core.graphics.*;
import albertgame.afengine.core.input.InputServlet;
import albertgame.afengine.core.message.Message;
import albertgame.afengine.core.message.MessageManager;
import albertgame.afengine.core.util.DebugUtil;
import albertgame.afengine.core.util.DebugUtil.LogType;
import albertgame.afengine.in.parts.input.InputRoute;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Albert Flex
 */
public class GraphicsTech_Java2DImpl2 implements IGraphicsTech {

    private static class JPanelTech extends JPanel {

        GraphicsTech_Java2DImpl2 tech;

        public JPanelTech(GraphicsTech_Java2DImpl2 impl) {
            tech = impl;
            super.setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            render(g2d, tech.width, tech.height);
            ++tech.count;
            tech.end = System.currentTimeMillis();
//            System.out.println("draw times:" + (tech.end - tech.start));
        }

        protected void render(Graphics2D g2d, int w, int h) {
            tech.graphics = g2d;
            g2d.setBackground(Color.BLACK);
            g2d.clearRect(0, 0, w, h);
            tech.setColor(tech.nowColor);
            tech.setFont(tech.nowFont);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Iterator<Map.Entry<Long, IDrawStrategy>> entryiter = tech.beforeStrategy.entrySet().iterator();
            while (entryiter.hasNext()) {
                IDrawStrategy d = entryiter.next().getValue();
                d.draw(tech);
            }

            if (tech.rootStrategy != null) {
                tech.rootStrategy.draw(tech);
            }

            entryiter = tech.afterStrategy.entrySet().iterator();
            while (entryiter.hasNext()) {
                IDrawStrategy d = entryiter.next().getValue();
                d.draw(tech);
            }
        }
    }

    private final java.util.List<IWindowAdjustHandler> handlerlist = new ArrayList<>();

    private static JFrame window;

    private ITexture icon;
    private String title;
    private boolean isFull;

    private int width, height;
    private int mheight, mwidth;

    private int x, y;

    private IFont nowFont;
    private IColor nowColor;
    private ITexture nowCursor;

    private Graphics2D graphics;
    private boolean isRendering;
    private int renderFPS;
    private Map<String, Object[]> valueMap;

    private JPanelTech content;

    private IDrawStrategy rootStrategy;
    private Map<Long, IDrawStrategy> beforeStrategy;
    private Map<Long, IDrawStrategy> afterStrategy;

    private int count;
    Runnable runnable = () -> {
        // task to run goes here
        renderFPS = count;
        count = 0;
        System.out.println("fps:" + renderFPS);
    }; //创建 run 方法
    ScheduledExecutorService service = Executors
            .newSingleThreadScheduledExecutor();

    public GraphicsTech_Java2DImpl2() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        mwidth = kit.getScreenSize().width;
        mheight = kit.getScreenSize().height;
        nowFont = new FontImpl("Dialog", false, IFont.FontStyle.PLAIN, 20);
        nowColor = new ColorImpl(IColor.GeneraColor.GREEN);
        isFull = false;
        isRendering = false;
        renderFPS = 0;
        valueMap = new HashMap<>();
        beforeStrategy = new HashMap<>();
        afterStrategy = new HashMap<>();
        service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void create(int x, int y, int w, int h, ITexture icon, String title) {
        if (window != null) {
            window.dispose();
        }
        window = new JFrame(title);
        if (icon != null) {
            if (icon instanceof TextureImpl) {
                TextureImpl texture = (TextureImpl) icon;
                window.setIconImage(texture.getImage());
            }
        }
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        content = new JPanelTech(this);
        window.getContentPane().add(content);
        window.setSize(w, h);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setLocation(x, y);
        this.width = w;
        this.height = h;
        this.x = x;
        this.y = y;
        addListeners();
    }

    @Override
    public void create(int w, int h, ITexture icon, String title) {
        if (window != null) {
            window.dispose();
        }
        window = new JFrame(title);
        JFrame frame = window;
        if (icon != null) {
            if (icon instanceof TextureImpl) {
                TextureImpl texture = (TextureImpl) icon;
                window.setIconImage(texture.getImage());
            }
        }
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        content = new JPanelTech(this);
        frame.getContentPane().add(content);
        frame.setBackground(Color.BLACK);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.x = frame.getLocation().x;
        this.y = frame.getLocation().y;
        this.width = w;
        this.height = h;
        addListeners();
    }

    @Override
    public void create(ITexture icon, String title) {
        GraphicsEnvironment env
                = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        if (window != null) {
            window.dispose();
        }
        window = new JFrame(title);
        content = new JPanelTech(this);
        window.getContentPane().add(content);
        window.setUndecorated(true);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        device.setFullScreenWindow(window);
        window.setResizable(false);
        DisplayMode displayMode = device.getDisplayMode();
        if (displayMode != null
                && device.isDisplayChangeSupported()) {
            try {
                device.setDisplayMode(displayMode);
            } catch (IllegalArgumentException ex) {
            }
            // fix for mac os x
            window.setSize(displayMode.getWidth(),
                    displayMode.getHeight());
        }
        width = displayMode.getWidth();
        height = displayMode.getHeight();
        x = y = 0;
        addListeners();
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        window.setTitle(title);
    }

    private void tick(boolean newfull, int newx, int newy, int newwidth, int newheight) {
        Iterator<IWindowAdjustHandler> iter = handlerlist.iterator();
        while (iter.hasNext()) {
            IWindowAdjustHandler handler = iter.next();
            handler.handleWindowAdjust(isFull, x, y, width, height, newfull, newx, newy, newwidth, newheight);
        }
    }

    @Override
    public void setIcon(ITexture texture) {
        this.icon = texture;
        window.setIconImage(((TextureImpl) texture).getImage());
    }

    @Override
    public void adjust(int x, int y, int w, int h) {
        window.setSize(width, height);
        window.setLocation(x, y);
        tick(false, x, y, w, h);
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    @Override
    public void adjust(int w, int h) {
        if (w > mwidth) {
            width = mwidth;
        } else {
            width = w;
        }
        if (h > mheight) {
            height = mheight;
        } else {
            height = h;
        }

        int dx = mwidth / 2 - width / 2;
        int dy = mheight / 2 - height / 2;
        adjust(dx, dy, w, h);
    }

    @Override
    public void adjustFull() {
        tick(true, 0, 0, this.getMoniterWidth(), this.getMoniterHeight());
        this.width = this.getMoniterWidth();
        this.height = this.getMoniterHeight();
        window.setLocation(0, 0);
        this.x = this.y = 0;
    }

    @Override
    public void addAdjustHandler(IWindowAdjustHandler handler) {
        this.handlerlist.add(handler);
    }

    @Override
    public void destroy() {
        if (window != null) {
            window.dispose();
        }
    }

    @Override
    public void moveWindowTo(int x, int y) {
        if (this.isFull) {
            DebugUtil.log(LogType.ERROR, "can not move ,cause full window.");
            return;
        }
        window.setLocation(x, y);
        this.x = x;
        this.y = y;
    }

    @Override
    public int getMoniterWidth() {
        return mwidth;
    }

    @Override
    public int getMoniterHeight() {
        return mheight;
    }

    @Override
    public int getDisplayX() {
        return x;
    }

    @Override
    public boolean isFullWindow() {
        return isFull;
    }

    @Override
    public int getDisplayY() {
        return y;
    }

    @Override
    public int getWindowWidth() {
        return this.content.getWidth();
    }

    @Override
    public int getWindowHeight() {
        return this.content.getHeight();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ITexture getIcon() {
        return icon;
    }

    @Override
    public ITexture getMouseIcon() {
        return this.nowCursor;
    }

    @Override
    public void setMouseIcon(ITexture texture) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Cursor cu = tk.createCustomCursor(((TextureImpl) texture).getImage(), new Point(10, 10), "mouse");
        window.setCursor(cu);
        nowCursor = texture;
    }

    @Override
    public String getRenderName() {
        return "Java2D";
    }

    @Override
    public IFont getFont() {
        return this.nowFont;
    }

    @Override
    public void setFont(IFont font) {
        this.nowFont = font;
        if (graphics != null) {
            graphics.setFont(((FontImpl) font).font);
        }
    }

    @Override
    public IColor getColor() {
        return this.nowColor;
    }

    @Override
    public void setColor(IColor color) {
        this.nowColor = color;
        if (graphics != null) {
            graphics.setColor(((ColorImpl) color).color);
        }
    }

    @Override
    public Object[] getValue(String name) {
        return this.valueMap.get(name);
    }

    @Override
    public void setValue(String name, Object[] obj) {
        this.valueMap.put(name,obj);
    }

    @Override
    public ITexture createTexture(String iconPath) {
        return new TextureImpl(iconPath);
    }

    @Override
    public ITexture createTexture(String iconPath, int cutFromX, int cutFromY, int cutWidth, int cutHeight) {
        ITexture te = new TextureImpl(iconPath);
        return te.cutInstance(cutFromX, cutFromY, cutWidth, cutHeight);
    }

    @Override
    public ITexture createTexture(URL url) {
        return new TextureImpl(url);
    }

    @Override
    public ITexture createTexture(URL url, int cutFromX, int cutFromY, int cutWidth, int cutHeight) {
        ITexture te = new TextureImpl(url);
        return te.cutInstance(cutFromX, cutFromY, cutWidth, cutHeight);
    }

    @Override
    public IFont createFont(String fontName, IFont.FontStyle style, int size) {
        return new FontImpl(fontName, false, style, size);
    }

    @Override
    public IFont createFontByPath(String fontPath, IFont.FontStyle style, int size) {
        return new FontImpl(fontPath, true, style, size);
    }

    @Override
    public IFont createFont(URL url, IFont.FontStyle style, int size) {
        return new FontImpl(url, style, size);
    }

    @Override
    public IColor createColor(int red, int green, int blue, int alpha) {
        return new ColorImpl(red, green, blue, alpha);
    }

    @Override
    public IColor createColor(IColor.GeneraColor colortype) {
        return new ColorImpl(colortype);
    }

    @Override
    public boolean isDrawing() {
        return isRendering;
    }

    long start = 0;
    long end = 0;

    @Override
    public void callDraw() {
            content.repaint();
    }

    @Override
    public void setRootDrawStrategy(IDrawStrategy strategy) {
        this.rootStrategy = strategy;
    }

    @Override
    public void addBeforeDrawStrategy(long priority, IDrawStrategy drawstrategy) {
        this.beforeStrategy.put(priority, drawstrategy);
    }

    @Override
    public void addAfterDrawStrategy(long priority, IDrawStrategy drawstrategy) {
        this.afterStrategy.put(priority, drawstrategy);
    }

    @Override
    public void clear(int x, int y, int width, int height, IColor color) {
        Color oldc = graphics.getColor();
        graphics.setColor(((ColorImpl) color).getColor());
        graphics.clearRect(x, y, width, height);
        graphics.setColor(oldc);
    }

    @Override
    public void drawPoint(int x, int y) {
        graphics.drawRect((int) x, (int) y, 1, 1);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    @Override
    public void drawPolygon(int[] x, int[] y, boolean fill) {
        int[] px = new int[x.length];
        int[] py = new int[x.length];
        for (int i = 0; i != px.length; ++i) {
            px[i] = ((int) x[i]);
            py[i] = (int) y[i];
        }

        if (fill) {
            graphics.fillPolygon(px, py, px.length);
        } else {
            graphics.drawPolygon(px, py, px.length);
        }
    }

    @Override
    public void drawOval(int x, int y, int w, int h, boolean fill) {
        if (fill) {
            graphics.fillOval(x, y, w, h);
        } else {
            graphics.drawOval(x, y, w, h);
        }
    }

    @Override
    public void drawCircle(int x, int y, int radius, boolean fill) {
        if (fill) {
            graphics.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        } else {
            graphics.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
        }
    }

    @Override
    public void drawTexture(int x, int y, ITexture texture) {
        graphics.drawImage(((TextureImpl) texture).getImage(), (int) x, (int) y, null);
    }

    @Override
    public void drawText(int x, int y, IFont font, IColor color, String text) {
        Font oldf = graphics.getFont();
        Color oldc = graphics.getColor();
        graphics.setColor(((ColorImpl) color).getColor());
        graphics.setFont(((FontImpl) font).getFont());
        graphics.drawString(text, x, y + font.getFontHeight());

        graphics.setFont(oldf);
        graphics.setColor(oldc);
    }

    @Override
    public void drawTexts(int[] x, int[] y, IFont[] font, IColor[] color, String[] text) {
        Font oldf = graphics.getFont();
        Color oldc = graphics.getColor();
        for (int i = 0; i != x.length; ++i) {
            graphics.setFont(((FontImpl) font[i]).getFont());
            graphics.setColor(((ColorImpl) color[i]).getColor());
            graphics.drawString(text[i], x[i], y[i] - font[i].getFontHeight());
        }
        graphics.setFont(oldf);
        graphics.setColor(oldc);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, boolean fill) {
        if (fill) {
            graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        } else {
            graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
    }

    @Override
    public void drawOther(String drawType, Object[] value) {
        if (drawType.equals("transformtexture")) {
            //ITexture texture,int x,int y,float ax,float ay,
            //double sx,double sy,int degree
            ITexture texture = (ITexture) value[0];
            int x = (int) value[1];
            int y = (int) value[2];
            double ax = (double) value[3];
            double ay = (double) value[4];
            double sx = (double) value[5];
            double sy = (double) value[6];
            int degree = (int) value[7];
            AffineTransform trans = new AffineTransform();
            double w0 = ax * texture.getWidth();
            w0 *= sx;
            double h0 = ay * texture.getHeight();
            h0 *= sy;
            double sins = Math.sin(Math.toRadians(degree));
            double coss = Math.cos(Math.toRadians(degree));
            float dx = (float) (x + sins * h0 - w0 * coss);
            float dy = (float) (y - coss * h0 - w0 * sins);
            trans.translate(dx, dy);
            trans.scale(sx, sy);
            trans.rotate(Math.toRadians(degree));
            graphics.drawImage(((TextureImpl) texture).getImage(), trans, null);
        } else {
            System.out.println("Do not support this type:" + drawType);
        }
    }

    @Override
    public int getFPS() {
        return this.renderFPS;
    }

    private class ColorImpl implements IColor {

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
            int w = (int) (getWidth() * sx);
            int h = (int) (getHeight() * sy);
            BufferedImage bi = createCompatibleImage(w, h);
            Graphics2D grph = (Graphics2D) bi.getGraphics();
            grph.drawImage(img, 0, 0, w, h,
                    0, 0, img.getWidth(null), img.getHeight(null), null);
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
            return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private void addListeners(){
        InputAdapter adapter = new InputAdapter();
        window.addKeyListener(adapter);
        window.addMouseListener(adapter);
        window.addMouseMotionListener(adapter);
        window.addMouseWheelListener(adapter);
        window.addWindowListener(adapter);
    }
    class InputAdapter implements KeyListener, MouseListener,
            MouseMotionListener, MouseWheelListener, WindowListener {

        private void sendMsg(long type, Object ... obj) {
            Message msg = new Message(InputRoute.Route_Input, type, 0, "",obj);
            MessageManager.getInstance().pushMessage(msg);
        }

        private void sendMsg(long type) {
            Message msg = new Message(InputRoute.Route_Input, type, 0, "", new Object[]{});
            MessageManager.getInstance().pushMessage(msg);
        }

        //content-eventcode,exobj[keychar]
        @Override
        public void keyTyped(KeyEvent e) {
            //DebugUtil.log("key typed " + e.getKeyChar());
//        System.out.println("key type");
            sendMsg(InputServlet.EventCode_KeyType, e.getKeyChar());
        }

        //content-eventcode,exobj[keycode]
        @Override
        public void keyPressed(KeyEvent e) {
            //DebugUtil.log("key pressed " + e.paramString());
            sendMsg(InputServlet.EventCode_KeyDown, e.getKeyCode());
        }

        //content-eventcode,exobj[keycode]
        @Override
        public void keyReleased(KeyEvent e) {
            //DebugUtil.log("key released " + e.paramString());
            sendMsg(InputServlet.EventCode_KeyUp,e.getKeyCode());
        }

        //content-eventcode,exobj[mousex,mousey]
        @Override
        public void mouseClicked(MouseEvent e) {
            //DebugUtil.log("mouse clicked " + e.paramString());
            sendMsg(InputServlet.EventCode_MouseClick, e.getX(), e.getY(),e.getButton());
        }

        //content-eventcode,exobj[mousex,mousey,keycode]
        @Override
        public void mousePressed(MouseEvent e) {
            //DebugUtil.log("mouse pressed " + e.paramString());
            sendMsg(InputServlet.EventCode_MouseDown, e.getX(), e.getY(), e.getButton());
        }

        //content-eventcode,exobj[mousex,mousey,keycode]
        @Override
        public void mouseReleased(MouseEvent e) {
            //DebugUtil.log("mouse released " + e.paramString());
            sendMsg(InputServlet.EventCode_MouseUp, e.getX(), e.getY(), e.getButton());
        }

        //content-eventcode
        @Override
        public void mouseEntered(MouseEvent e) {
            //DebugUtil.log("mouse enter windows");
            sendMsg(InputServlet.EventCode_MouseInWindow);
        }

        //content-eventcode
        @Override
        public void mouseExited(MouseEvent e) {
            //DebugUtil.log("mouse exit windows");
            sendMsg(InputServlet.EventCode_MouseExitWindow);
        }

        //content-eventcode,exobjs[mousex,mousey]
        @Override
        public void mouseDragged(MouseEvent e) {
            //DebugUtil.log("mouse dragged to [" + e.getX() + "," + e.getY() + "]");
            sendMsg(InputServlet.EventCode_MouseDrag, e.getX(), e.getY());
        }

        //content-eventcode,exobjs[mousex,mousey]
        @Override
        public void mouseMoved(MouseEvent e) {
//        //DebugUtil.log("mouse moved to ["+e.getX()+","+e.getY()+"]") ;
            sendMsg(InputServlet.EventCode_MouseMove, e.getX(), e.getY());
        }

        //content-eventcode
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int r = e.getWheelRotation();
            if (r > 0) {
                //DebugUtil.log("mousewheel up");
                sendMsg(InputServlet.EventCode_MouseWheelUp);
            } else if (r < 0) {
                //DebugUtil.log("mousewheel down");
                sendMsg(InputServlet.EventCode_MouseWheelDown);
            }
        }

        @Override
        public void windowOpened(WindowEvent e) {
            //DebugUtil.log("window opened");
//        sendMsg(InputServlet.EventCode_WindowOpened);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            //DebugUtil.log("window closing");
//        sendMsg(InputServlet.EventCode_WindowClosing);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            //DebugUtil.log("window closed");
//        sendMsg(InputServlet.EventCode_WindowClose);
        }

        @Override
        public void windowIconified(WindowEvent e) {
            //DebugUtil.log("window iconified");
            sendMsg(InputServlet.EventCode_WindowIconed);
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            //DebugUtil.log("window deiconified");
            sendMsg(InputServlet.EventCode_WindowDeiconed);
        }

        @Override
        public void windowActivated(WindowEvent e) {
            //DebugUtil.log("window actived");
            sendMsg(InputServlet.EventCode_WindowActive);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            //DebugUtil.log("window deactived");
            sendMsg(InputServlet.EventCode_WindowDeactive);
        }
    }

}
