package pala.gui.dialog;
import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JFrame {
    
    private int duration;
    
    public SplashScreen(int d) {
        duration = d;
    }
    
    // A simple little method to show a title screen in the center
    // of the screen for the amount of time given in the constructor
    public void showSplash() {
        
        JPanel content = (JPanel)getContentPane();
        content.setBackground(Color.white);
        
        // Set the window's bounds, centering the window
        int width = 450;
        int height =115;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);
        
        // Build the splash screen
        JLabel label = new JLabel(new ImageIcon("C:\\Users\\Phuoc Dang\\git\\pala-finance\\src\\main\\resources\\piggy-bank-icon.png"));
        JLabel copyrt = new JLabel
                ("Copyright 2002, O'Reilly & Associates", JLabel.CENTER);
        copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.SOUTH);
        Color oraRed = new Color(156, 20, 20,  255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 10));
        
        // Display it
        setVisible(true);
        
        // Wait a little while, maybe while loading resources
        //try { Thread.sleep(duration); } catch (Exception e) {}
        
        //setVisible(false);
        
    }
}