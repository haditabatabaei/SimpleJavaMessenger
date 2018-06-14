package client;

public class ClientStart {
    public static boolean isLoggedIn = false;
    public static final String DEFAULT_SERVER = "138.201.143.20";
    public static final String LOCAL = "127.0.0.1";
    public static final int DEFAULT_PORT = 8080;
    public static final int DEFAULT_PORT_REG = 8181;
    public static final int DEFAULT_PORT_LOG = 8182;
    public static final int RESPONSE_GOOD = 1;
    public static final int RESPONSE_BAD_NOUSER = -1;
    public static final int RESPONSE_BAD_CONNECTION = 0;

    public static void main(String[] args) {
        ClientGui gui = new ClientGui();
        gui.makeVisible();
    }
}