package client;

import client.clientHandlers.RegisterHandler;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame {
    public static JFrame frame;
    private JPanel contentPanel;
    private JLabel userNameLabel;
    public static JTextField userNameField;
    private JLabel passLabel;
    public static JPasswordField passField;
    private JLabel emailLabel;
    public static JTextField emailField;
    private JLabel firstNameLabel;
    public static JTextField firstNameField;
    private JLabel lastNameLabel;
    public static JTextField lastNameField;
    public static JComboBox ageRangeCombo;
    private JLabel ageRangeLabel;
    private JButton registerButton;
    private JButton cancelButton;
    private JButton clearAllButton;
    private RegisterHandler rh;

    public RegisterFrame() {
        frame = new JFrame("Register Window");
        fillContentPanel();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().add(contentPanel);
        frame.pack();
        frame.setResizable(false);
    }

    public void makeVisible() {
        frame.setVisible(true);
    }

    private void fillContentPanel() {
        contentPanel = new JPanel(null);

        //construct preComponents
        String[] ageRangeComboItems = {"under 18", "18", "+18"};

        //construct components
        userNameLabel = new JLabel("Username:");
        userNameField = new JTextField(5);
        passLabel = new JLabel("Password:");
        passField = new JPasswordField(5);
        emailLabel = new JLabel("Email:");
        emailField = new JTextField(5);
        firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(5);
        lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(5);
        ageRangeCombo = new JComboBox(ageRangeComboItems);
        ageRangeLabel = new JLabel("Age range:");
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
        clearAllButton = new JButton("Clear All");

        rh = new RegisterHandler();
        registerButton.setActionCommand(ClientGui.COMMAND_REGISTER);
        cancelButton.setActionCommand(ClientGui.COMMAND_CANCEL);
        clearAllButton.setActionCommand(ClientGui.COMMAND_CLEAR_ALL);


        registerButton.addActionListener(rh);
        cancelButton.addActionListener(rh);
        clearAllButton.addActionListener(rh);

        //set components properties
        userNameLabel.setToolTipText("enter your username");
        userNameField.setToolTipText("enter your username here");
        passLabel.setToolTipText("password");
        passField.setToolTipText("enter your password here");
        emailLabel.setToolTipText("email");
        emailField.setToolTipText("enter your email here");
        firstNameField.setToolTipText("enter your first name here");
        lastNameField.setToolTipText("enter your last name here");
        ageRangeCombo.setToolTipText("select your age range");
        ageRangeLabel.setToolTipText("age range");
        registerButton.setToolTipText("click to register");
        cancelButton.setToolTipText("click to cancel");
        clearAllButton.setToolTipText("click to erase all data");

        //adjust size and set layout
        contentPanel.setPreferredSize(new Dimension(397, 269));

        //add components
        contentPanel.add(userNameLabel);
        contentPanel.add(userNameField);
        contentPanel.add(passLabel);
        contentPanel.add(passField);
        contentPanel.add(emailLabel);
        contentPanel.add(emailField);
        contentPanel.add(firstNameLabel);
        contentPanel.add(firstNameField);
        contentPanel.add(lastNameLabel);
        contentPanel.add(lastNameField);
        contentPanel.add(ageRangeCombo);
        contentPanel.add(ageRangeLabel);
        contentPanel.add(registerButton);
        contentPanel.add(cancelButton);
        contentPanel.add(clearAllButton);

        //set component bounds (only needed by Absolute Positioning)
        userNameLabel.setBounds(15, 35, 65, 35);
        userNameField.setBounds(90, 40, 265, 25);
        passLabel.setBounds(15, 70, 65, 25);
        passField.setBounds(90, 70, 265, 25);
        emailLabel.setBounds(25, 100, 65, 25);
        emailField.setBounds(90, 100, 265, 25);
        firstNameLabel.setBounds(15, 130, 100, 25);
        firstNameField.setBounds(90, 130, 265, 25);
        lastNameLabel.setBounds(15, 160, 100, 25);
        lastNameField.setBounds(90, 160, 265, 25);
        ageRangeCombo.setBounds(90, 190, 100, 25);
        ageRangeLabel.setBounds(15, 190, 100, 25);
        registerButton.setBounds(300, 230, 85, 35);
        cancelButton.setBounds(195, 240, 100, 25);
        clearAllButton.setBounds(90, 240, 100, 25);

    }
}
