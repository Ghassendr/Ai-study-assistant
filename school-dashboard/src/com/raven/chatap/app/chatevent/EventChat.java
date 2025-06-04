package com.raven.chatap.app.chatevent;

import com.raven.chatap.app.chatmodel.Model_Receive_Message;
import com.raven.chatap.app.chatmodel.Model_Send_Message;

public interface EventChat {

    public void sendMessage(Model_Send_Message data);

    public void receiveMessage(Model_Receive_Message data);
}
