package jpanel;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    JPanel panel;
    Thread RenderThread;
    Image img;
    public GameWindow(){
        img = new ImageIcon("E:\\Game_Engine\\resource\\images\\player1.png").getImage();
        panel = new Container();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 400);
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.getContentPane().add(panel);
        //this.setBounds(100, 100, 400, 400);
//        this.setIgnoreRepaint(true);
        this.setVisible(true);



    }
}
class Container extends JPanel{

public Container(){
    Thread Thread = new Thread(){
        @Override
        public void run() {
            while (true){
                repaint();
            }
        }
    };
    Thread.start();
}
    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.BLACK);
        g2d.clearRect(0, 0, 800, 400);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.translate(0, 400 - 20);
        g2d.setColor(Color.RED);
        g2d.drawString("GameWindow", 100, 100);
    }
}