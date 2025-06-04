package com.raven.chatap.app.chatevent;

import com.raven.chatap.app.chatmodel.Model_Login;
import com.raven.chatap.app.chatmodel.Model_Register;

public interface EventLogin {

    public void login(Model_Login data);

    public void register(Model_Register data, EventMessage message);

    public void goRegister();

    public void goLogin();
}
