package server;


import com.sun.deploy.util.StringUtils;

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
            System.out.println("listening on port " + PORT);
            serverSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println("Waiting for register connection...");
                Socket tmpSocket = serverSocket.accept();
                System.out.println("Register connection from  " + tmpSocket.getInetAddress().getHostAddress());
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
                System.out.println("Try Reading register info...");
                String fromClient = dataInputStream.readUTF();
                StringTokenizer tokenizer = new StringTokenizer(fromClient, "\n");
                System.out.println("from client : " + fromClient);
                int newLineCounter = 0;
                for (int i = 0; i < fromClient.length(); i++)
                    if (fromClient.charAt(i) == '\n') {
                        System.out.println("Char  at " + i + "is backslash n");
                        newLineCounter++;
                    }
                System.out.println("number of total newlines : " + newLineCounter);
                System.out.println("Sub String length : " + tokenizer.countTokens());
                System.out.println("---------------STARTING FOR-----------");
                while (tokenizer.hasMoreTokens()) {
                    String thisToken = tokenizer.nextToken();
                    System.out.println("This Token :" + thisToken);
                    System.out.println("------------------------");
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
                    System.out.println("adding account...");
                    ServerStart.addAccount(new Account(user, pass, email, fullName, age));
                    System.out.println("Account added.");
                    dataOutputStream.writeUTF("Account has been registered.Thank You.");
                    dataOutputStream.flush();
                    myClient.close();
                } else {
                    System.out.println("Duplicate user or pass or email.account exists somehow.server did not register account.");
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
                System.out.println("Register streamers ready.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
