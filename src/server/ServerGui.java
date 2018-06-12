package server;

import server.serverHandlers.ServerCommandHandler;

import javax.swing.*;
import java.awt.*;

public class ServerGui {
    public static JFrame frame;
    public static JTextArea textArea;
    public static JTextField textField;
    private JPanel northPanel;
    private JLabel portLabel;
    private JButton startButton;
    private JButton stopButton;
    private JScrollPane scrollPane;
    private JPanel southPanel;
    public static final String COMMAND_START = "start";
    public static final String COMMAND_STOP = "stop";
    private static ServerCommandHandler serverCommandHandler;

    public ServerGui() {
        frameStart();
        northStart();
        centerAndSouthStart();
        addStuff();
    }

    private void addStuff() {
        northPanel.add(portLabel, BorderLayout.WEST);
        northPanel.add(textField, BorderLayout.CENTER);
        northPanel.add(startButton, BorderLayout.EAST);

        southPanel.add(stopButton, BorderLayout.EAST);

        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
    }

    private void frameStart() {
        frame = new JFrame("Simple Java Messenger - Server");
        Dimension frameDimension = new Dimension(540, 630);
        frame.setSize(frameDimension);
        frame.setMinimumSize(frameDimension);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void northStart() {
        serverCommandHandler = new ServerCommandHandler();
        northPanel = new JPanel(new BorderLayout());
        portLabel = new JLabel("Port:");
        textField = new JTextField();
        startButton = new JButton("Start");
        startButton.setActionCommand(COMMAND_START);
        startButton.addActionListener(serverCommandHandler);
    }

    private void centerAndSouthStart() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        southPanel = new JPanel(new BorderLayout());
        stopButton = new JButton("Stop Server");
        stopButton.setActionCommand(COMMAND_STOP);
        stopButton.addActionListener(serverCommandHandler);
    }

    void makeVisible() {
        frame.setVisible(true);
    }
}
