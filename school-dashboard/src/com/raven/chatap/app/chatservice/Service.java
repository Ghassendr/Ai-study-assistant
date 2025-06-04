package com.raven.chatap.app.chatservice;

import com.raven.chatap.app.chatevent.EventFileReceiver;
import com.raven.chatap.app.chatevent.PublicEvent;
import com.raven.chatap.app.chatmodel.Model_File_Receiver;
import com.raven.chatap.app.chatmodel.Model_File_Sender;
import com.raven.chatap.app.chatmodel.Model_Receive_Message;
import com.raven.chatap.app.chatmodel.Model_Send_Message;
import com.raven.chatap.app.chatmodel.Model_User_Account;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Service {

    private static Service instance;
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "192.168.56.1";
    private Model_User_Account user;
    private final List<Model_File_Sender> fileSender;
    private final List<Model_File_Receiver> fileReceiver;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public Service() {
        fileSender = new ArrayList<>();
        fileReceiver = new ArrayList<>();
    }

    public void startServer() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);

            client.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("[Service] Connecté au serveur !");
                }
            });

            client.on("list_user", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    List<Model_User_Account> users = new ArrayList<>();
                    for (Object o : os) {
                        Model_User_Account u = new Model_User_Account(o);
                        if (user == null || u.getUserID() != user.getUserID()) {
                            users.add(u);
                        }
                    }
                    PublicEvent.getInstance().getEventMenuLeft().newUser(users);
                }
            });

            client.on("user_status", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int userID = (Integer) os[0];
                    boolean status = (Boolean) os[1];
                    if (status) {
                        PublicEvent.getInstance().getEventMenuLeft().userConnect(userID);
                    } else {
                        PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userID);
                    }
                }
            });

            client.on("receive_ms", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Receive_Message message = new Model_Receive_Message(os[0]);
                    PublicEvent.getInstance().getEventChat().receiveMessage(message);
                }
            });

            client.open();
        } catch (URISyntaxException e) {
            error(e);
        }
    }

    public Model_File_Sender addFile(File file, Model_Send_Message message) throws IOException {
        Model_File_Sender data = new Model_File_Sender(file, client, message);
        message.setFile(data);
        fileSender.add(data);
        if (fileSender.size() == 1) {
            data.initSend();
        }
        return data;
    }

    public void fileSendFinish(Model_File_Sender data) throws IOException {
        fileSender.remove(data);
        if (!fileSender.isEmpty()) {
            fileSender.get(0).initSend();
        }
    }

    public void fileReceiveFinish(Model_File_Receiver data) throws IOException {
        fileReceiver.remove(data);
        if (!fileReceiver.isEmpty()) {
            fileReceiver.get(0).initReceive();
        }
    }

    public void addFileReceiver(int fileID, EventFileReceiver event) throws IOException {
        Model_File_Receiver data = new Model_File_Receiver(fileID, client, event);
        fileReceiver.add(data);
        if (fileReceiver.size() == 1) {
            data.initReceive();
        }
    }

    public void setUser(Model_User_Account user) {
        this.user = user;
    }

    public Model_User_Account getUser() {
        return user;
    }

    public Socket getClient() {
        return client;
    }

    public void loadUsers() {
        if (client != null && client.connected()) {
            client.emit("list_user");
        } else {
            System.out.println("[Service] Impossible de charger les utilisateurs : socket non connecté");
        }
    }

    private void error(Exception e) {
        System.err.println("[Service] Erreur : " + e.getMessage());
    }
}
