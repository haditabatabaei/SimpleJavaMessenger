package client;

import client.clientHandlers.ExportFrameHandler;

import javax.swing.*;
import java.awt.*;

public class ExportFrame {
    public static JFrame frame;
    private JLabel pathLabel;
    public static JTextField pathField;
    public static JFileChooser fileChooser;
    private JButton browseButton;
    private JButton okButton;
    private JButton cancelButton;


    private JPanel topPanel;
    private JPanel pathPanel;
    private JPanel buttonsPanel;
    private ExportFrameHandler exportFrameHandler;

    public ExportFrame() {
        frameStart();
        northStart();
        buttonsPanelStart();
        toolsStuff();
        addingStuff();
    }

    private void frameStart() {
        frame = new JFrame("Export Settings");
        frame.setSize(500, 125);
        frame.setMinimumSize(new Dimension(500, 125));
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void northStart() {
        pathLabel = new JLabel("Save Path:");
        pathField = new JTextField();
        fileChooser = new JFileChooser();
        browseButton = new JButton("Browse");



        pathPanel = new JPanel(new BorderLayout());


        topPanel = new JPanel(new BorderLayout());

    }

    private void buttonsPanelStart() {
        buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
    }

    private void addingStuff() {
        pathPanel.add(pathLabel, BorderLayout.WEST);
        pathPanel.add(pathField, BorderLayout.CENTER);
        pathPanel.add(browseButton, BorderLayout.EAST);


        topPanel.add(pathPanel, BorderLayout.NORTH);
        topPanel.add(buttonsPanel, BorderLayout.CENTER);

        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        frame.add(topPanel, BorderLayout.NORTH);
    }

    private void toolsStuff() {
        exportFrameHandler = new ExportFrameHandler();
        okButton.setActionCommand(ClientGui.COMMAND_EXPORT);
        cancelButton.setActionCommand(ClientGui.COMMAND_CANCEL);
        browseButton.setActionCommand(ClientGui.COMMAND_ACTIVE_FILE_CHOOSER);

        okButton.addActionListener(exportFrameHandler);
        cancelButton.addActionListener(exportFrameHandler);
        browseButton.addActionListener(exportFrameHandler);

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Choose a directory to save :");
    }

    public void makeVisible() {
        frame.setVisible(true);
    }
}
