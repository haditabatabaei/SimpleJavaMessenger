package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerLoginManager extends Thread implements Runnable {
    ServerSocket serverSocket;

    public void run() {
        listen();
    }

    private void listen() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(8182);
            executorService.execute(new HandleClientLogin(serverSocket.accept()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class HandleClientLogin extends Thread implements Runnable {
        Socket myClient;

        public HandleClientLogin(Socket client) {
            myClient = client;
        }

        public void run() {
            try {
                DataInputStream dataInputStream = new DataInputStream(myClient.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(myClient.getOutputStream());
                String messageFromClient = dataInputStream.readUTF();
                String username = "";
                String password = "";
                StringTokenizer tokenizer = new StringTokenizer(messageFromClient, "\n");
                while (tokenizer.hasMoreTokens()) {
                    String thisToken = tokenizer.nextToken();
                    if (thisToken.startsWith("[USERNAME]"))
                        username = thisToken.substring(10);
                    else
                        password = thisToken.substring(6);
                }

                if (hasAccount(username, password)) {
                    dataOutputStream.writeInt(1);
                } else {
                    dataOutputStream.writeInt(-1);
                }
                dataOutputStream.flush();
            } catch (IOException e) {

            }
        }

        private boolean hasAccount(String username, String password) {
            for (Account account : ServerStart.accounts) {
                if (account.getUserName().equals(username))
                    if (account.getPassword().equals(password)) {
                        account.setLoggedIn(true);
                        return true;
                    }
            }
            return false;
        }
    }
}
