package com.ruslan.task_ok.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}