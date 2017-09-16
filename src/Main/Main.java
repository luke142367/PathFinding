package Main;

import javax.swing.*;

/**
 * Created by LUKE_2 on 03/10/2015.
 */
public class Main extends JFrame{

    public static void main(String[] args){
        new Main();
    }

    public Main(){
        super("Path Finding");
        this.setContentPane(new Panel(this));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
