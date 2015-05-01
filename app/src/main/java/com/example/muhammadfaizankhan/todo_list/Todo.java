package com.example.muhammadfaizankhan.todo_list;

import android.util.Log;

import java.util.GregorianCalendar;

/**
 * Created by Muhammad Faizan Khan on 11/04/2015.
 */
public class Todo {
    private boolean done;
    private String description;
    private GregorianCalendar dateTime;
    User byUser;
    Group ofGroup;
    User[] forUsers;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    Todo(String description,boolean done){
        Log.i("Todo:Counstructor ", "todo obj created");
        this.description=description;
        this.done=done;
        dateTime=new GregorianCalendar();
    }

    Todo(String description,boolean done,GregorianCalendar dateTime){
        Log.i("Todo:Counstructor ", "todo obj created");
        this.description=description;
        this.done=done;
        this.dateTime=dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
    public GregorianCalendar getDateTime() {
        return dateTime;
    }

}
