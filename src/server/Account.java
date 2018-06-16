package server;

import java.net.Socket;

public class Account {
    private String fullName;
    private String userName;
    private String password;
    private String email;
    private int age;
    private int id;
    private boolean isLoggedIn;
    private Socket socket;
    private String socketIp;

    public Account(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        isLoggedIn = false;
    }

    public Account(String userName, String password, String email, String fullName, int age) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.age = age;
        isLoggedIn = false;
    }

    public String getSocketIp() {
        return socketIp;
    }

    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > -1)
            this.id = id;
    }

    public String toStringForSave() {
        String toReturn;
        toReturn = "username:" + userName + "\npassword:" + password + "\nfullname:" + fullName + "\nemail:" + email + "\nage:" + age + "\nid:" + id + "\nip:" + socketIp + "\n[END]\n";
        return toReturn;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public void print() {
        System.out.println("+------------------------------------------------+");
        System.out.println("|Username:" + userName);
        System.out.println("|Password:" + password);
        System.out.println("|Full Name:" + fullName);
        System.out.println("|Email:" + email);
        System.out.println("|Age:" + age);
        System.out.println("|ID:" + id);
        System.out.println("|IP:" + socketIp);
        System.out.println("|IsOnline:" + isLoggedIn + "");
        System.out.println("+------------------------------------------------+");
    }
}
