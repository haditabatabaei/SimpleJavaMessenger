package server;

import java.net.Socket;
import java.util.ArrayList;

public class ServerStart {
    private static ServerGui gui;
    public static ArrayList<Socket> clients;

    public static void main(String[] args) {
        gui = new ServerGui();
        gui.makeVisible();
        clients = new ArrayList<>();
    }
}
