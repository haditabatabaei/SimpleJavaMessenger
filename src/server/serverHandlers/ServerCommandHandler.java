package server.serverHandlers;

import server.Account;
import server.ServerGui;
import server.ServerStart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCommandHandler implements ActionListener {
    private ServerRunnerHandler serverRunnerHandler;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JButton) e.getSource()).getActionCommand()) {
            case ServerGui.COMMAND_START:
                if (canHandlePort(getServerPort())) {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    serverRunnerHandler = new ServerRunnerHandler(getServerPort());
                    executorService.execute(serverRunnerHandler);
                    ServerGui.startButton.setEnabled(false);
                    ServerGui.textField.setEnabled(false);
                    ServerGui.stopButton.setEnabled(true);
                }
                break;
            case ServerGui.COMMAND_STOP:
                serverRunnerHandler.stopServer();
                ServerGui.stopButton.setEnabled(false);
                ServerGui.startButton.setEnabled(true);
                ServerGui.textField.setEnabled(true);
                break;
            case ServerGui.COMMAND_PRINT_ACCOUNTS:
                for (Account account : ServerStart.accounts) {
                    account.print();
                }
        }
    }

    private int getServerPort() {
        int port;
        String fieldInput = ServerGui.textField.getText().replaceAll(" ", "");
        if (!fieldInput.isEmpty()) {
            try {
                port = Integer.parseInt(fieldInput);
                return port;
            } catch (Exception e) {
                ServerGui.textArea.append("port field contains no integer number.\n");
                return 0;
            }
        } else {
            return 0;
        }
    }

    private boolean canHandlePort(int inputPort) {
        return inputPort > 0 && inputPort <= 65500;
    }

    class ServerRunnerHandler extends Thread implements Runnable {
        int serverPort;
        ServerSocket serverSocket;
        ExecutorService executorService;

        public ServerRunnerHandler(int newPort) {
            serverPort = newPort;
            executorService = Executors.newCachedThreadPool();
        }

        public void stopServer() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                ServerGui.textArea.append("Server has been bound.\n");
                serverSocket = new ServerSocket(serverPort);
                while (true) {
                    Socket tmpSocket;
                    ServerGui.textArea.append("Waiting for connection...\n");
                    tmpSocket = serverSocket.accept();
                    ServerGui.textArea.append("Connection received from : " + tmpSocket.getInetAddress().getHostAddress() + "\n");
                    ServerStart.clients.add(tmpSocket);
                    MultiClientHandler tmpHandler = new MultiClientHandler(tmpSocket);
                    executorService.execute(tmpHandler);
                }
            } catch (IOException e) {
                e.printStackTrace();
                ServerGui.textArea.append("Something wrong with the server binding.\n");
            }
        }

    }

    class MultiClientHandler extends Thread implements Runnable {
        Socket clientSocket;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        public MultiClientHandler(Socket client) {
            clientSocket = client;
        }

        public void run() {
            getStreamers();
            listen();
        }

        void listen() {
            try {
                String line = "";
                String toRead = "";
                System.out.println("Going inside while socket is not closed.");
                while (!clientSocket.isClosed()) {
                    System.out.println("Reading...");
                    toRead = dataInputStream.readUTF();
                    System.out.println("Reading completed.");
                    ServerGui.textArea.append(clientSocket.getInetAddress().getHostAddress() + ">" + toRead + "\n");
                    sendToAll(toRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void sendToAll(String message) {
            for (Socket client : ServerStart.clients) {
                if (clientSocket != client) {
                    DataOutputStream tmpDataOutputStream;
                    try {
                        tmpDataOutputStream = new DataOutputStream(client.getOutputStream());
                        tmpDataOutputStream.writeUTF(clientSocket.getInetAddress().getHostAddress() + ">" + message);
                        tmpDataOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        void getStreamers() {
            try {
                ServerGui.textArea.append("Getting streamers for " + clientSocket.getInetAddress().getHostAddress() + "\nWaiting...\n");
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                ServerGui.textArea.append("Streamers ready.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
