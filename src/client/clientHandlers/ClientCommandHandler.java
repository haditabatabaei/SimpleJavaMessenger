package client.clientHandlers;

import client.ClientGui;
import client.ExportFrame;
import client.LoginFrame;
import client.RegisterFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ClientCommandHandler implements ActionListener {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ReaderHandler readerHandler;
    private ExportFrame exportFrame;
    private RegisterFrame registerFrame;
    private LoginFrame loginFrame;

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
                if (registerFrame == null)
                    registerFrame = new RegisterFrame();
                registerFrame.makeVisible();
                break;
            case ClientGui.COMMAND_EXPORT:
                exportFrame = new ExportFrame();
                exportFrame.makeVisible();
                break;
            case ClientGui.COMMAND_EXIT:
                System.exit(0);
                break;

            case ClientGui.COMMAND_LOGIN:
                if (loginFrame == null)
                    loginFrame = new LoginFrame();
                loginFrame.makeVisible();
                break;
            case ClientGui.COMMAND_LOGOUT:
                //logout operation;
                break;

        }
    }

    private void connectOperation() {
        if (isValidIp()) {
            int port;
            String hostIp;
            String serverIp = ClientGui.comboBox.getSelectedItem().toString();
            port = Integer.parseInt(serverIp.substring(serverIp.indexOf(":") + 1));
            hostIp = serverIp.substring(0, serverIp.indexOf(":"));
            ClientGui.textArea.append("HOST IP : " + hostIp + " | PORT : " + port + "\n");
            ClientGui.comboBox.setEnabled(false);
            ClientGui.connectButton.setEnabled(false);
            try {
                ClientGui.textArea.append("Trying to connect to server...\n");
                socket = new Socket(hostIp, port);
                ClientGui.textArea.append("Connected to server.\n");
                ClientGui.textArea.append("Getting streamers...\n");

                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                ClientGui.textArea.append("Streamers ready.\n");
                ClientGui.textArea.append("Starting reading from server thread...");
                ExecutorService executorService = Executors.newCachedThreadPool();
                readerHandler = new ReaderHandler();
                ClientGui.textArea.append("Starting Thread...\n");
                executorService.execute(readerHandler);
                ClientGui.textArea.append("Thread apparently launched.\n");

            } catch (IOException e) {
                //e.printStackTrace();
                ClientGui.textArea.append("Some error happened. socket error.\n");
                ClientGui.comboBox.setEnabled(true);
                ClientGui.connectButton.setEnabled(true);
            }
        } else
            ClientGui.textArea.append("ip is not valid.check your address again\n");
    }

    private void sendOperation() {
        try {
            if (socket.isConnected()) {
                String clientInput = ClientGui.messageField.getText();
                // printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                if (!clientInput.isEmpty()) {
                    try {
                        System.out.println("sending message to server...");
                        dataOutputStream.writeUTF(clientInput);
                        dataOutputStream.flush();
                        ClientGui.textArea.append("ME>" + clientInput + "\n");
                    } catch (Exception e) {
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
                    ClientGui.comboBox.setEnabled(true);
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
        String serverInfo = ClientGui.comboBox.getSelectedItem().toString().replaceAll(" ", "");

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

    private void exportOperation() {

    }

    class ReaderHandler extends Thread implements Runnable {
        public void run() {
            String toRead;
            while (true) {
                try {
                    toRead = dataInputStream.readUTF();
                    ClientGui.textArea.append(toRead + "\n");
                    sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
