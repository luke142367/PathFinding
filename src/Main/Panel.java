package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by LUKE_2 on 03/10/2015.
 */
public class Panel extends JPanel implements Runnable, MouseListener, MouseMotionListener,KeyListener {
    private BufferedImage image;
    private Main main;
    private int Width = 800,Height = 800;
    private int targetTps = 80;
    private boolean running = false;
    private Graphics2D g;
    private Thread thread;
    private PathFinder pathFinder;

    public Panel(Main main) {
        this.main = main;
        setPreferredSize(new Dimension(Width,Height));
        setFocusable(true);
        requestFocus();
        init();
    }

    private void init() {
        image = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D)image.getGraphics();
        pathFinder = new PathFinder(Width,Height);
        //pathFinder.StartSearch();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void run() {
        int fps = 0; int tick = 0;
        double fpsTimer = System.currentTimeMillis();
        double secondsPerTick = 1D / targetTps;
        double nsPerTick = secondsPerTick * 1000000000D;
        double then = System.nanoTime();
        double now;
        double unprocessed = 0;
        while(running){
            now = System.nanoTime();
            unprocessed += (now - then) ;
            then = now;
            while(unprocessed >= nsPerTick){
                update();
                tick++;
                unprocessed -= nsPerTick;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
            fps++;
            if(System.currentTimeMillis() - fpsTimer >= 1000){
                //System.out.printf("FPS: %d, TPS: %d %n", fps, tick);
                fps = 0;
                tick = 0;
                fpsTimer += 1000;
            }
        }
    }

    private void update() {
        pathFinder.update();
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Width, Height);
        g.setColor(Color.BLACK);
        draw((Graphics2D) g);
        g.drawImage(image, 0, 0, Width, Height, null);
    }

    private void draw(Graphics2D g) {
        pathFinder.draw(g);
    }

    public void mouseClicked(MouseEvent e) {
        pathFinder.mouseClicked(e);
    }

    public void mouseReleased(MouseEvent e) {
        pathFinder.mouseReleased(e);
    }

    public void mouseDragged(MouseEvent e) {
        pathFinder.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            pathFinder.StartSearch();
        }
        if(e.getKeyCode() == KeyEvent.VK_UP){
            pathFinder.up();
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pathFinder.down();
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            pathFinder.cancel();
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            pathFinder.Init();
        }
        if(e.getKeyCode() == KeyEvent.VK_M){
            pathFinder.Menu();
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
