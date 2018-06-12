package client.clientHandlers;

import client.ClientGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class ClientCommandHandler implements ActionListener {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton;
        JMenuItem clickedItem;
        String command;
        if (e.getSource() instanceof JButton) {
            clickedButton = (JButton) e.getSource();
            command = clickedButton.getActionCommand();
        } else {
            clickedItem = (JMenuItem) e.getSource();
            command = clickedItem.getActionCommand();
        }
        switch (command) {
            case ClientGui.COMMAND_CONNECT:
                connectOperation();
                break;
            case ClientGui.COMMAND_DISCONNECT:
                disconnectOperation();
                break;
            case ClientGui.COMMAND_SEND:
                sendOperation();
                break;
            case ClientGui.COMMAND_ABOUT:
                //AboutOperation to do.
                break;
            case ClientGui.COMMAND_HELP:
                //helpOperation();
                break;
            case ClientGui.COMMAND_REGISTER:
                //registerOperation();
                break;
            case ClientGui.COMMAND_EXPORT:
                //exportOperation();
                break;
            case ClientGui.COMMAND_EXIT:
                System.exit(0);
                break;
        }
    }

    private void connectOperation() {
        if (isValidIp()) {
            int port;
            String hostIp;
            String serverIp = ClientGui.serverField.getText().replaceAll(" ", "");
            port = Integer.parseInt(serverIp.substring(serverIp.indexOf(":") + 1));
            hostIp = serverIp.substring(0, serverIp.indexOf(":"));
            ClientGui.textArea.append("HOST IP : " + hostIp + " | PORT : " + port + "\n");
            ClientGui.serverField.setText(hostIp + ":" + port);
            ClientGui.serverField.setEditable(false);
            ClientGui.connectButton.setEnabled(false);
            try {
                ClientGui.textArea.append("Trying to connect to server...\n");
                socket = new Socket(hostIp, port);
                ClientGui.textArea.append("Connected to server.\n");
                ClientGui.textArea.append("Getting streamers...\n");
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                ClientGui.textArea.append("Streamers ready.\n");
            } catch (IOException e) {
                e.printStackTrace();
                ClientGui.textArea.append("Some error happened. socket error.\n");
                ClientGui.serverField.setEditable(true);
                ClientGui.connectButton.setEnabled(true);
            }
        } else
            ClientGui.textArea.append("ip is not valid.check your address again\n");
    }

    private void sendOperation() {
        try {
            if (socket.isConnected()) {
                String clientInput = ClientGui.messageField.getText().trim();
                if (!clientInput.isEmpty()) {
                    try {
                        System.out.println("sending message to server...");
                        bufferedWriter.write(clientInput + "\n");
                        ClientGui.textArea.append("ME>" + clientInput + "\n");
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Some error writing to server.");
                    }
                }
            } else {
                ClientGui.textArea.append("There is no connection to server.");
            }
        } catch (NullPointerException npe) {
            ClientGui.textArea.append("No Socket connection to send message.\n");
        }

    }

    private void disconnectOperation() {
        try {
            if (socket.isConnected()) {
                try {
                    System.out.println("Closing writer...");
                    bufferedWriter.close();
                    System.out.println("Writer closed.");
                    System.out.println("Closing reader...");
                    bufferedReader.close();
                    System.out.println("Reader closed.");
                    System.out.println("Closing socket...");
                    socket.close();
                    System.out.println("Socket closed.");
                    ClientGui.serverField.setEditable(true);
                    ClientGui.connectButton.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Some errors happened closing streamers or socket.");
                }
            } else {
                ClientGui.textArea.append("socket is not connected.\n");
            }
        } catch (NullPointerException ne) {
            ClientGui.textArea.append("No socket connection to disconnect from.\n");
        }

    }

    private boolean isValidIp() {
        String serverInfo = ClientGui.serverField.getText().replaceAll(" ", "");

        Pattern p = Pattern.compile("^"
                + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
                + "|"
                + "localhost" // localhost
                + "|"
                + "(([0-9]{1,3}\\.){3})[0-9]{1,3})" // Ip
                + ":"
                + "[0-9]{1,5}$");// Port


        if (serverInfo.isEmpty())
            return false;
        else {
            return p.matcher(serverInfo).matches();
        }
    }
}
