package com.ruslan.task_ok.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ruslan.task_ok.client.NotLoggedInException;
import com.ruslan.task_ok.client.TaskService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TaskServiceImpl extends RemoteServiceServlet implements TaskService {

  private static final Logger LOG = Logger.getLogger(TaskServiceImpl.class.getName());
  private static final PersistenceManagerFactory PMF =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");

  public void addTask(String name, String text) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      pm.makePersistent(new Task(getUser(), name, text));
    } finally {
      pm.close();
    }
  }

  public void removeTask(String name) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      long deleteCount = 0;
      Query q = pm.newQuery(Task.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      List<Task> tasks = (List<Task>) q.execute(getUser());
      for (Task task : tasks) {
        if (name.equals(task.getName())) {
          deleteCount++;
          pm.deletePersistent(task);
        }
      }
      if (deleteCount != 1) {
        LOG.log(Level.WARNING, "removeTask deleted " + deleteCount + " Tasks");
      }
    } finally {
      pm.close();
    }
  }

  public String[] getTasks() throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    List<String> names = new ArrayList<String>();
    try {
      Query q = pm.newQuery(Task.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      q.setOrdering("createDate");
      List<Task> tasks = (List<Task>) q.execute(getUser());
      for (Task task : tasks) {
        names.add(task.getName());
      }
    } finally {
      pm.close();
    }
    return (String[]) names.toArray(new String[0]);
  }

  private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }

  private User getUser() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.getCurrentUser();
  }

  private PersistenceManager getPersistenceManager() {
    return PMF.getPersistenceManager();
  }
}