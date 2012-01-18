package com.ruslan.task_ok.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Task_ok implements EntryPoint {
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private VerticalPanel logoutPanel = new VerticalPanel();
	private Label logoutLabel = new Label("e-mail");
    private Anchor signInLink = new Anchor("Вход");
    private Anchor signOutLink = new Anchor("Выход");
    private final TaskServiceAsync taskService = GWT.create(TaskService.class);
    private FlexTable tasksFlexTable = new FlexTable();
    private VerticalPanel mainPanel = new VerticalPanel();
    private TextBox newNameTextBox = new TextBox();
    private TextBox newTextTextBox = new TextBox();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private Button addTaskButton = new Button("Добавить");
    private ArrayList<String> tasks = new ArrayList<String>();
    private VerticalPanel statPanel = new VerticalPanel();
	private Label statLabel = new Label("ok");
    
	/**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
		// Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
        statLabel.setText("вход в аккаунт...");
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      
	      public void onFailure(Throwable error) {
	    	  handleError(error);
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	          statLabel.setText("успешный вход в аккаунт");
	       	  loadTask_ok();
	        } else {
	          statLabel.setText("запрос аккаунта");
	          loadLogin();
	        }
	      }
	    });
    }
	
    private void loadLogin() {
	    // Assemble login panel.
	    signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(signInLink);
	    RootPanel.get("loginDiv").add(loginPanel);
	}
	
	private void loadTask_ok() {

		statLabel.setText("test");
		statPanel.add(statLabel);
	    RootPanel.get("statusDiv").add(statPanel);
	    
		// Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    logoutLabel.setText(loginInfo.getEmailAddress());
	    logoutPanel.add(logoutLabel);
	    logoutPanel.add(signOutLink);
	    RootPanel.get("loginDiv").add(logoutPanel);
	    
	    // Create table for task data.
	    tasksFlexTable.setText(0, 0, "Задача");
	    tasksFlexTable.setText(0, 1, "Описание");

	    // Add styles to elements in the task list table.
	    tasksFlexTable.setCellPadding(6);
	    tasksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
	    tasksFlexTable.addStyleName("watchList");

	    // Assemble Add New Task panel.
	    addPanel.add(newNameTextBox);
	    addPanel.add(newTextTextBox);
	    addPanel.add(addTaskButton);
	    addPanel.addStyleName("addPanel");

	    // Assemble Main panel.
	    mainPanel.add(tasksFlexTable);
	    mainPanel.add(addPanel);

	    // Associate the Main panel with the HTML host page.
	    RootPanel.get("mainDiv").add(mainPanel);
	    
	    loadTasks();
	    
	    // Listen for mouse events on the Add button.
	    addTaskButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        addNewTask();
	      }
	    });

	    // Listen for keyboard events in the input box.
	    newNameTextBox.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	          addNewTask();
	        }
	      }
	    });

	    // Listen for keyboard events in the input box.
	    newTextTextBox.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	          addNewTask();
	        }
	      }
	    });
	}
	
	private void addNewTask() {
	    final String name = newNameTextBox.getText();
	    final String text = newTextTextBox.getText();
	    newNameTextBox.setFocus(true);

	    newNameTextBox.setText("");
	    newTextTextBox.setText("");

	    // Don't add the task if it's already in the table.
	    if (tasks.contains(name))
	      return;

	    addTask(name, text);
	}

	private void addTask(final String name, final String text) {
        statLabel.setText("добавление задачи...");
	    taskService.addTask(name, text, new AsyncCallback<Void>() {
	      public void onFailure(Throwable error) {
	    	  handleError(error);
	      }
	      public void onSuccess(Void ignore) {
            statLabel.setText("добавлена задача '" + name + "'");
	        displayTask(name, text);
	      }
	    });
	}

	private void displayTask(final String name, final String text) {
	    // Add the task to the table.
	    int row = tasksFlexTable.getRowCount();
	    tasks.add(name);
	    tasksFlexTable.setText(row, 0, name);
	    tasksFlexTable.setText(row, 1, text);
	    tasksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListRemoveColumn");

	    // Add a button to remove this task from the table.
	    Button removeTaskButton = new Button("x");
	    removeTaskButton.addStyleDependentName("remove");
	    removeTaskButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	removeTask(name);
	      }
	    });
	    tasksFlexTable.setWidget(row, 2, removeTaskButton);
	}
	
	private void removeTask(final String name) {
            statLabel.setText("удаление задачи...");
		    taskService.removeTask(name, new AsyncCallback<Void>() {
		      public void onFailure(Throwable error) {
		    	  handleError(error);
		      }
		      public void onSuccess(Void ignore) {
	            statLabel.setText("удалена задача '" + name + "'");
		        undisplayTask(name);
		      }
		    });
	}

	private void undisplayTask(String name) {
		    int removedIndex = tasks.indexOf(name);
		    tasks.remove(removedIndex);
		    tasksFlexTable.removeRow(removedIndex + 1);
	}

    private void loadTasks() {
        statLabel.setText("запрос списка задач...");
	    taskService.getTasks(new AsyncCallback<String[]>() {
	      public void onFailure(Throwable error) {
	    	  handleError(error);
	      }
	      public void onSuccess(String[] names) {
            statLabel.setText("загружен список задач (всего " + names.length + ")");
	        displayTasks(names);
	      }
	    });
	}

	private void displayTasks(String[] names) {
	    for (int i = 0; i < names.length; i++) {
		  displayTask(names[i], "");
	    }
	}

	private void handleError(Throwable error) {
	    statLabel.setText(error.getMessage());
		Window.alert(error.getMessage());
	    if (error instanceof NotLoggedInException) {
	      Window.Location.replace(loginInfo.getLogoutUrl());
	    }
	}
}
