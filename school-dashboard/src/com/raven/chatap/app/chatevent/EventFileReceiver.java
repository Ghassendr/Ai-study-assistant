package com.raven.chatap.app.chatevent;

import java.io.File;

public interface EventFileReceiver {

    public void onReceiving(double percentage);

    public void onStartReceiving();

    public void onFinish(File file);
}
