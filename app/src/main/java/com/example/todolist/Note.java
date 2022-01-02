package com.example.todolist;

public class Note {

    private int id;  //It is something that will be used when we store our note inside SQLite database. So every record, every note must have some kind of ID which will be used later when we want to delete specific note->Each id will be unique
    private String title;
    private String description;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
