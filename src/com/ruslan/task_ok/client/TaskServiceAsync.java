package com.ruslan.task_ok.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TaskServiceAsync {
  public void addTask(String name, String descr, AsyncCallback<Void> async);
  public void removeTask(String name, AsyncCallback<Void> async);
  public void getTasks(AsyncCallback<String[]> async);
}
