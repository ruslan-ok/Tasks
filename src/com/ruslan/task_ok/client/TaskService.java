package com.ruslan.task_ok.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {
  public void addTask(String name, String descr) throws NotLoggedInException;
  public void removeTask(String name) throws NotLoggedInException;
  public String[] getTasks() throws NotLoggedInException;
}
