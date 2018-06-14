package client.clientHandlers;

import client.ClientGui;
import client.ExportFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportFrameHandler implements ActionListener {
    private File targetFile;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JButton) e.getSource()).getActionCommand()) {
            case ClientGui.COMMAND_EXPORT:
                String savePath = ExportFrame.pathField.getText() + "\\";
                targetFile = new File(savePath + "ExportedJavaMessenger.txt");

                try {
                    if (targetFile.createNewFile())
                        System.out.println("file created.");

                    FileWriter fileWriter = new FileWriter(targetFile);
                    fileWriter.write(ClientGui.textArea.getText());
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Export completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case ClientGui.COMMAND_CANCEL:
                ExportFrame.frame.setVisible(false);
                break;
            case ClientGui.COMMAND_ACTIVE_FILE_CHOOSER:
                JFileChooser myChooser = ExportFrame.fileChooser;
                int result = myChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    if (myChooser.getSelectedFile().isDirectory()) {
                        ExportFrame.pathField.setText(myChooser.getSelectedFile().getAbsolutePath() + "");
                    }
                }
                break;
        }
    }
}
