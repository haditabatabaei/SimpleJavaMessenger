package client.clientHandlers;

import client.ClientGui;
import client.ClientStart;
import client.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JButton) e.getSource()).getActionCommand()) {
            case ClientGui.COMMAND_LOGIN:
                if (!ClientStart.isLoggedIn) {
                    connectAngLogin();
                } else
                    JOptionPane.showMessageDialog(null, "You already logged in.", "Error", JOptionPane.WARNING_MESSAGE);
                break;
            case ClientGui.COMMAND_CANCEL:
                LoginFrame.frame.setVisible(false);
                break;
            case ClientGui.COMMAND_CLEAR_ALL:
                clearAllOperation();
                break;
        }
    }


    private void clearAllOperation() {
        LoginFrame.usernameField.setText("");
        LoginFrame.passwordField.setText("");
    }

    private void connectAngLogin() {
        try {
            String guiUsername = LoginFrame.usernameField.getText();
            String guipass = LoginFrame.passwordField.getText();
            Socket socket = new Socket(ClientStart.DEFAULT_SERVER, ClientStart.DEFAULT_PORT_LOG);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String toSend = "[USERNAME]" + guiUsername + "\n[PASS]" + guipass;
            dataOutputStream.writeUTF(toSend);
            dataOutputStream.flush();
            int response = dataInputStream.readInt();
            if (response == ClientStart.RESPONSE_GOOD) {
                JOptionPane.showMessageDialog(null, "You logged in.\nThank you", "Login", JOptionPane.INFORMATION_MESSAGE);
                ClientStart.isLoggedIn = true;
                socket.close();
            } else if (response == ClientStart.RESPONSE_BAD_NOUSER)
                JOptionPane.showMessageDialog(null, "Error logging in.\nSorry.\nCheck your username and password or register.", "Error", JOptionPane.WARNING_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Error in internet connection with server.\ncheck your connection or call us.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            ClientGui.textArea.append("Some error happened connection to server for login\n");
        }
    }
}
