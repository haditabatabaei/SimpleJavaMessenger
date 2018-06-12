package server.serverHandlers;

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
    ServerRunnerHandler serverRunnerHandler;
    ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JButton) e.getSource()).getActionCommand()) {
            case ServerGui.COMMAND_START:
                if (canHandlePort(getServerPort())) {
                    serverRunnerHandler = new ServerRunnerHandler(getServerPort());
                    executorService.execute(serverRunnerHandler);
                }
                break;
            case ServerGui.COMMAND_STOP:
                //stop server operation;
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
        if (inputPort <= 0) {
            ServerGui.textArea.append("zero or less than zero port or no available input.\n");
            return false;
        } else return inputPort <= 65000;
    }

    class ServerRunnerHandler extends Thread implements Runnable {
        int serverPort;
        ServerSocket serverSocket;
        ExecutorService executorService;

        public ServerRunnerHandler(int newPort) {
            serverPort = newPort;
            executorService = Executors.newCachedThreadPool();
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

        public MultiClientHandler(Socket client) {
            clientSocket = client;
        }

        public void run() {
            getStreamers();
            while (clientSocket.isConnected()) {
                try {
                    String line = "";
                    String toRead = "";
                    System.out.println("Reading...");
                    while ((line = bufferedReader.readLine()) != null) {
                        toRead += line;
                    }
                    ServerGui.textArea.append(clientSocket.getInetAddress().getHostAddress() + ">" + toRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void getStreamers() {
            try {
                ServerGui.textArea.append("Getting streamers for " + clientSocket.getInetAddress().getHostAddress() + "\nWaiting...\n");
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                ServerGui.textArea.append("Streamers ready.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
