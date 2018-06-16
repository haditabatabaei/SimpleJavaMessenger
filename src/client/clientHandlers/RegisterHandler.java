package client.clientHandlers;

import client.ClientGui;
import client.ClientStart;
import client.RegisterFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class RegisterHandler implements ActionListener {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("[a-zA-Z0-9]+[._a-zA-Z0-9!#$%&'*+-/=?^_`{|}~]*[a-zA-Z]*@[a-zA-Z0-9]{2,8}.[a-zA-Z.]{2,6}");
    String user;
    String pass;
    String email;
    String firstN;
    String lastN;
    String ageRange;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JButton) e.getSource()).getActionCommand()) {
            case ClientGui.COMMAND_REGISTER:
                user = RegisterFrame.userNameField.getText();
                pass = RegisterFrame.passField.getText();
                email = RegisterFrame.emailField.getText();
                firstN = RegisterFrame.firstNameField.getText();
                lastN = RegisterFrame.lastNameField.getText();
                ageRange = RegisterFrame.ageRangeCombo.getSelectedIndex() + "";
                boolean validForRegister = true;
                if (!user.isEmpty() && pass.length() >= 8 && !email.isEmpty() && !firstN.isEmpty() && !lastN.isEmpty()) {
                    if (!email.matches(VALID_EMAIL_ADDRESS_REGEX.toString()))
                        validForRegister = false;
                } else
                    validForRegister = false;


                if (validForRegister) {
                    System.out.println("Register function called");
                    connectToServerForRegister();
                } else {
                    String messgae = "One or more below errors happened:\n1.All fields must be filled up.\n2.Password must be more than 8 characters.\n3.Email address must be valid.\n[If you think this message showed on a wrong basis contact support.]\nTHANK YOU";
                    JOptionPane.showMessageDialog(null, messgae, "Something went wrong", JOptionPane.WARNING_MESSAGE);
                }
                break;
            case ClientGui.COMMAND_CLEAR_ALL:
                RegisterFrame.userNameField.setText("");
                RegisterFrame.passField.setText("");
                RegisterFrame.emailField.setText("");
                RegisterFrame.firstNameField.setText("");
                RegisterFrame.lastNameField.setText("");
                RegisterFrame.ageRangeCombo.setSelectedItem(RegisterFrame.ageRangeCombo.getItemAt(0));
                RegisterFrame.frame.revalidate();
                break;
            case ClientGui.COMMAND_CANCEL:
                RegisterFrame.frame.setVisible(false);
                break;
        }
    }

    private void connectToServerForRegister() {
        DataInputStream dataInputStream;
        BufferedWriter bufferedWriter;
        DataOutputStream dataOutputStream;
        try {
            Socket socket = new Socket(ClientStart.DEFAULT_SERVER, ClientStart.DEFAULT_PORT_REG);
            dataInputStream = new DataInputStream(socket.getInputStream());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String toSend =
                    "[USER]" + user + "\n[PASS]" + pass + "\n[MAIL]" + email + "\n[NAME]" + firstN + " " + lastN + "\n[AGE]" + ageRange;
            dataOutputStream.writeUTF(toSend);
            bufferedWriter.flush();
            String responseMessage = dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, responseMessage);
            socket.close();

        } catch (IOException e) {
            ClientGui.textArea.append("Some error happened connection to server for register");
        }
    }
}
