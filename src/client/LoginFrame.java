package client;

import client.clientHandlers.LoginHandler;

import javax.swing.*;
import java.awt.*;

public class LoginFrame {
    public static JFrame frame;
    private JPanel contentPanel;
    public static JButton loginButton;
    public static JButton cancelButton;
    public static JButton clearAllButton;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    public static JTextField usernameField;
    public static JPasswordField passwordField;
    private LoginHandler loginHandler;

    public LoginFrame() {
        startFrame();
        createComponents();
        toolsStuff();
        addingStuff();
    }

    private void createComponents() {
        //construct components
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        clearAllButton = new JButton("Clear All");
        userNameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(5);
        passwordField = new JPasswordField(5);
    }

    private void toolsStuff() {
        //set components properties
        loginButton.setToolTipText("click to login");
        cancelButton.setToolTipText("click to cancel");
        clearAllButton.setToolTipText("click to reset all fields");
        userNameLabel.setToolTipText("user name");
        passwordLabel.setToolTipText("password");
        usernameField.setToolTipText("enter your username");
        passwordField.setToolTipText("enter your password here");

        //set component bounds (only needed by Absolute Positioning)
        loginButton.setBounds(250, 125, 110, 55);
        cancelButton.setBounds(145, 155, 100, 25);
        clearAllButton.setBounds(145, 125, 100, 25);
        userNameLabel.setBounds(15, 25, 70, 25);
        passwordLabel.setBounds(15, 60, 70, 25);
        usernameField.setBounds(80, 25, 235, 25);
        passwordField.setBounds(80, 60, 235, 25);

        //adding actions
        loginHandler = new LoginHandler();

        loginButton.setActionCommand(ClientGui.COMMAND_LOGIN);
        clearAllButton.setActionCommand(ClientGui.COMMAND_CLEAR_ALL);
        cancelButton.setActionCommand(ClientGui.COMMAND_CANCEL);

        loginButton.addActionListener(loginHandler);
        clearAllButton.addActionListener(loginHandler);
        cancelButton.addActionListener(loginHandler);

    }

    private void startFrame() {
        frame = new JFrame("Login");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setResizable(false);
        contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(364, 183));
        frame.getContentPane().add(contentPanel);
        frame.pack();
    }


    private void addingStuff() {
        //add components
        contentPanel.add(loginButton);
        contentPanel.add(cancelButton);
        contentPanel.add(clearAllButton);
        contentPanel.add(userNameLabel);
        contentPanel.add(passwordLabel);
        contentPanel.add(usernameField);
        contentPanel.add(passwordField);
    }

    public void makeVisible() {
        frame.setVisible(true);
    }
}
