package com.raven.chatap.app.chatevent;

import com.raven.chatap.app.chatmodel.Model_User_Account;

public interface EventMain {

    public void showLoading(boolean show);

    public void initChat();

    public void selectUser(Model_User_Account user);

    public void updateUser(Model_User_Account user);
}
