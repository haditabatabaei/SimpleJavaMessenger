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
            while (true) {
                ServerGui.textArea.append("Waiting for login connection...\n");
                Socket tmpSocket = serverSocket.accept();
                ServerGui.textArea.append("Login connection received from " + tmpSocket.getInetAddress().getHostAddress() + "\n");
                executorService.execute(new HandleClientLogin(tmpSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class HandleClientLogin extends Thread implements Runnable {
        Socket handleClientSocket;

        public HandleClientLogin(Socket loginSocket) {
            handleClientSocket = loginSocket;
        }

        public void run() {
            try {
                DataInputStream dataInputStream = new DataInputStream(handleClientSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(handleClientSocket.getOutputStream());
                String messageFromClient = dataInputStream.readUTF();
                String username = "";
                String password = "";
                StringTokenizer tokenizer = new StringTokenizer(messageFromClient, "\n");
                ServerGui.textArea.append("Extracting login message...\n");
                while (tokenizer.hasMoreTokens()) {
                    String thisToken = tokenizer.nextToken();
                    if (thisToken.startsWith("[USERNAME]"))
                        username = thisToken.substring(10);
                    else
                        password = thisToken.substring(6);
                }
                ServerGui.textArea.append("Extracting completed.\n");
                if (hasAccount(username, password)) {
                    Account foundAccount = ServerStart.findAccount(username);
                    foundAccount.setLoggedIn(true);
                    foundAccount.setSocketIp(handleClientSocket.getInetAddress().getHostAddress());
                    dataOutputStream.writeInt(1);
                    ServerGui.textArea.append(foundAccount.getFullName() + " Logged in.");
                } else {
                    dataOutputStream.writeInt(-1);
                }
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean hasAccount(String username, String password) {
            for (Account account : ServerStart.accounts) {
                if (account.getUserName().equals(username))
                    if (account.getPassword().equals(password)) {
                        return true;
                    }
            }
            return false;
        }
    }
}
