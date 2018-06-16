package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerAccounter extends Thread implements Runnable {
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private static int PORT = 8181;
    private ServerSocket serverSocket;

    public ServerAccounter() {
    }

    public void run() {
        listen();
    }

    private void listen() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                ServerGui.textArea.append("Waiting for register connection...\n");
                Socket tmpSocket = serverSocket.accept();
                ServerGui.textArea.append("Register connection from  " + tmpSocket.getInetAddress().getHostAddress() + "\n");
                executorService.execute(new HandleClientRegister(tmpSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class HandleClientRegister extends Thread implements Runnable {
        Socket myClient;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        BufferedReader bufferedReader;
        String user;
        String pass;
        String email;
        String fullName;
        int age;

        public HandleClientRegister(Socket socket) {
            myClient = socket;
        }

        public void run() {
            getStreamers();
            try {
                String fromClient = dataInputStream.readUTF();
                StringTokenizer tokenizer = new StringTokenizer(fromClient, "\n");
                ServerGui.textArea.append("Extracting user register info...\n");
                while (tokenizer.hasMoreTokens()) {
                    String thisToken = tokenizer.nextToken();
                    if (thisToken.startsWith("[USER]")) {
                        user = thisToken.substring(6);
                    } else if (thisToken.startsWith("[PASS]")) {
                        pass = thisToken.substring(6);
                    } else if (thisToken.startsWith("[MAIL]")) {
                        email = thisToken.substring(6);
                    } else if (thisToken.startsWith("[NAME]")) {
                        fullName = thisToken.substring(6);
                    } else if (thisToken.startsWith("[AGE]")) {
                        age = Integer.parseInt(thisToken.substring(5));
                    }
                }
                if (canRegister(user, email)) {
                    ServerStart.addAccount(new Account(user, pass, email, fullName, age));
                    dataOutputStream.writeUTF("Account has been registered.Thank You.");
                    dataOutputStream.flush();
                    myClient.close();
                    ServerGui.textArea.append("Account added.\n");
                } else {
                    dataOutputStream.writeUTF("Sorry.\nAn account with this username or email already exists.\nAccount did not registered.");
                    myClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean canRegister(String user, String email) {
            //check user duplicate
            for (Account account : ServerStart.accounts)
                if (account.getUserName().equals(user) || account.getEmail().equals(email))
                    return false;

            return true;
        }

        private void getStreamers() {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
                dataInputStream = new DataInputStream(myClient.getInputStream());
                dataOutputStream = new DataOutputStream(myClient.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
