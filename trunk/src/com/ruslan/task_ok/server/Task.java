package com.ruslan.task_ok.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Task {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private User user;
  @Persistent
  private String name;
  @Persistent
  private String text;
  @Persistent
  private Date createDate;

  public Task() {
    this.createDate = new Date();
  }

  public Task(User user, String name, String text) {
    this();
    this.user = user;
    this.name = name;
    this.text = text;
  }

  public Long getId() {
    return this.id;
  }

  public User getUser() {
    return this.user;
  }

  public String getName() {
	    return this.name;
	  }

  public String getText() {
	    return this.text;
	  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setText(String text) {
    this.text = text;
  }
}