package client;

import javax.swing.*;
import java.awt.*;

public class ClientGui extends Thread {
    protected static JFrame frame;

    public ClientGui() {
        frame = new JFrame("Simple Java Messenger - Client");
        frame.setLayout(new BorderLayout());
        frame.setSize(350, 350);
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void makeHidden() {
        frame.setVisible(false);
    }

    public static void makeVisible() {
        frame.setVisible(true);
    }
}
