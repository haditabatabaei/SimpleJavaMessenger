package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStart {
    private static ServerGui gui;
    public static ArrayList<Socket> clients;
    public static ArrayList<Account> accounts;
    public static int ACCOUNT_NUMBERS = -1;
    public static File accountsFile;
    public static final String RELATIVE_SAVE_PATH = "java-simple-messenger-server\\accounts";
    public static final String ACCOUNTS_FILE_NAME = "accounts.txt";

    public static void main(String[] args) {
        gui = new ServerGui();
        gui.makeVisible();
        clients = new ArrayList<>();
        accounts = new ArrayList<>();
        createSaveFile();
        loadAccounts();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ServerAccounter serverAccounter = new ServerAccounter();
        executorService.execute(serverAccounter);
    }

    private static void createSaveFile() {
        accountsFile = new File(RELATIVE_SAVE_PATH + "\\" + ACCOUNTS_FILE_NAME);
        File tmpDirectory = new File(RELATIVE_SAVE_PATH);
        if (tmpDirectory.mkdirs())
            System.out.println("Directory created.");
        else
            System.out.println("Directory exists.");

        try {
            if (accountsFile.createNewFile())
                System.out.println("Save file created.");
            else
                System.out.println("Save file exists.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addAccount(Account account) {
        accounts.add(account);
        ACCOUNT_NUMBERS++;
        account.setId(ACCOUNT_NUMBERS);
        saveAccountToFile(account);
    }

    private static void justAddAccount(Account account) {
        accounts.add(account);
        ACCOUNT_NUMBERS++;
        account.setId(ACCOUNT_NUMBERS);
    }

    private static void loadAccounts() {
        System.out.println("load accounts...");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(accountsFile));
            String line;

            String user = "", pass = "", email = "", fullname = "";
            int age = 0, id;
            System.out.println("Start reading from file...");
            String readFromFile = "";
            while ((line = bufferedReader.readLine()) != null) {
                readFromFile += line;
                if (line.startsWith("username:")) {
                    user = line.substring(9);
                } else if (line.startsWith("password:")) {
                    pass = line.substring(9);
                } else if (line.startsWith("fullname:")) {
                    fullname = line.substring(9);
                } else if (line.startsWith("email:")) {
                    email = line.substring(6);
                } else if (line.startsWith("age:")) {
                    age = Integer.parseInt(line.substring(4));
                } else if (line.startsWith("id:")) {
                    id = Integer.parseInt(line.substring(3));
                } else if (line.startsWith("[END]")) {
                    justAddAccount(new Account(user, pass, email, fullname, age));
                    System.out.println("Read until that id :" + readFromFile);
                } else {
                    System.out.println("Inside else");
                    break;
                }
            }
            System.out.println("Reading finished.");
            bufferedReader.close();
            System.out.println("Buffer closing.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveAccountToFile(Account account) {
        try {
            FileWriter fileWriter = new FileWriter(accountsFile, true);
            String toWrite = account.toStringForSave();
            fileWriter.write(toWrite);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
