package Frames;
import javax.swing.*;
import java.awt.*;

public class SplashScreenDemo {
    JFrame frame;
    JLabel image =new JLabel(new ImageIcon("C:\\Users\\dell\\Desktop\\M1_exercice\\Client_gui\\src\\Icons\\Logo_ThreadStorm_200.png"));


    JLabel text = new JLabel("THREADSTORM");
    JProgressBar progressBar = new JProgressBar();
    JLabel message = new JLabel();// Crating a JLabel for displaying the message

    SplashScreenDemo() {
        createGUI();
        addImage();
        addText();
        addProgressBar();
        addMessage();
        runningPBar();

    }

    public void createGUI() {
        frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.decode("#2D3F45"));
        frame.setVisible(true);

    }

    public void addImage() {
        image.setSize(600, 200);
        frame.add(image);
    }

    public void addText() {
        text.setFont(new Font("arial", Font.BOLD, 24));
        text.setBounds(170, 220, 600, 40);
        text.setForeground(Color.BLUE);
        frame.add(text);
    }

    public void addMessage() {
        message.setBounds(250, 320, 200, 40);// Setting the size and location of the label
        message.setForeground(Color.black);// Setting foreground Color
        message.setFont(new Font("arial", Font.BOLD, 15));// Setting font properties
        frame.add(message);// adding label to the frame
    }

    public void addProgressBar() {
        progressBar.setBounds(100, 280, 400, 30);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(Color.BLACK);
        progressBar.setValue(0);
        frame.add(progressBar);
    }

    public void runningPBar() {
        int i = 0;// Creating an integer variable and intializing it to 0

        while (i <= 100) {
            try {
                Thread.sleep(50);// Pausing execution for 50 milliseconds
                progressBar.setValue(i);// Setting value of Progress Bar
                message.setText("LOADING " + Integer.toString(i) + "%");// Setting text of the message JLabel
                i+=2;
                if (i == 100)
                    frame.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
