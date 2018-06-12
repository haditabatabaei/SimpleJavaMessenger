package client;

import javax.swing.*;
import java.awt.*;

public class ClientGui extends Thread {
    protected static JFrame frame;
    private JPanel topPanel;
    private JPanel downPanel;
    private JPanel serverInfoPanel;
    private JPanel messageInfoPanel;
    private JLabel serverLabel;
    private JLabel messageLabel;
    private JLabel disconnectLabel;
    protected static JTextField serverField;
    protected static JTextField messageField;
    protected static JButton connectButton;
    protected static JButton sendButton;
    protected static JButton disconnectButton;
    protected static JTextArea textArea;
    private JScrollPane textAreaScrollPane;
    private Dimension frameDimension;

    protected static final String COMMAND_CONNECT = "connect";
    protected static final String COMMAND_SEND = "send";
    protected static final String COMMAND_DISCONNECT = "disconnect";
    protected static final String COMMAND_EXPORT = "export";
    protected static final String COMMAND_EXIT = "exit";
    protected static final String COMMAND_REGISTER = "register";
    protected static final String COMMAND_ABOUT = "about";
    protected static final String COMMAND_HELP = "help";

    ClientGui() {
        frameStart();
        northStart();
        centerAndSouthStart();
        menuBarStuff();
        toolsStuff();
        addingStuff();
    }

    private void frameStart() {
        /*Frame Base stats*/
        frame = new JFrame("Simple Java Messenger - Client");
        frame.setLayout(new BorderLayout());
        /*Frame size */
        frameDimension = new Dimension(540, 630);
        frame.setSize(frameDimension);
        frame.setMinimumSize(frameDimension);
        /*Frame starting position to set on center using relative position to null*/
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void northStart() {
        topPanel = new JPanel(new BorderLayout());
        serverInfoPanel = new JPanel(new BorderLayout());
        messageInfoPanel = new JPanel(new BorderLayout());

        serverLabel = new JLabel("Server IP:");
        serverField = new JTextField();
        connectButton = new JButton("Connect");

        serverInfoPanel.add(serverLabel, BorderLayout.WEST);
        serverInfoPanel.add(serverField, BorderLayout.CENTER);
        serverInfoPanel.add(connectButton, BorderLayout.EAST);

        messageLabel = new JLabel("Message:");
        messageField = new JTextField();
        sendButton = new JButton("Send");

    }

    private void centerAndSouthStart() {
        textArea = new JTextArea();
        textAreaScrollPane = new JScrollPane(textArea);

        downPanel = new JPanel(new BorderLayout());
        disconnectLabel = new JLabel("if you want to disconnect from server click disconnect button :");
        disconnectButton = new JButton("Disconnect");
    }

    private void addingStuff() {
        messageInfoPanel.add(messageLabel, BorderLayout.WEST);
        messageInfoPanel.add(messageField, BorderLayout.CENTER);
        messageInfoPanel.add(sendButton, BorderLayout.EAST);

        topPanel.add(serverInfoPanel, BorderLayout.NORTH);
        topPanel.add(messageInfoPanel, BorderLayout.CENTER);

        downPanel.add(disconnectLabel, BorderLayout.CENTER);
        downPanel.add(disconnectButton, BorderLayout.EAST);


        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(textAreaScrollPane, BorderLayout.CENTER);
        frame.add(downPanel, BorderLayout.SOUTH);
    }

    private void toolsStuff() {
        connectButton.setActionCommand(COMMAND_CONNECT);
        sendButton.setActionCommand(COMMAND_SEND);
        disconnectButton.setActionCommand(COMMAND_DISCONNECT);

    }

    private void menuBarStuff() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem registerItem = new JMenuItem("Register");
        JMenuItem sendItem = new JMenuItem("Send");
        JMenuItem connectItem = new JMenuItem("Connect");
        JMenuItem disconnectItem = new JMenuItem("Disconnect");
        JMenuItem exportItem = new JMenuItem("Export");
        JMenuItem exitItem = new JMenuItem("Exit");

        helpItem.setActionCommand(COMMAND_HELP);
        aboutItem.setActionCommand(COMMAND_ABOUT);
        registerItem.setActionCommand(COMMAND_REGISTER);
        sendItem.setActionCommand(COMMAND_SEND);
        connectItem.setActionCommand(COMMAND_CONNECT);
        disconnectItem.setActionCommand(COMMAND_DISCONNECT);
        exportItem.setActionCommand(COMMAND_EXPORT);
        exitItem.setActionCommand(COMMAND_EXIT);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        fileMenu.add(connectItem);
        fileMenu.add(sendItem);
        fileMenu.add(exportItem);
        fileMenu.add(disconnectItem);
        fileMenu.add(exitItem);
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);
        helpMenu.add(registerItem);

        frame.setJMenuBar(menuBar);
    }

    public void makeHidden() {
        frame.setVisible(false);
    }

    void makeVisible() {
        frame.setVisible(true);
    }
}
