package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; //Basically, when we change the structure of our database, we should say that the database version is now some other number than 1, and it needs to be put in this constructor in here. So we will only need it for creating our database and for nothing else.

    private static final String DATABASE_NAME = "NotesDatabase";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {  //will be called when we create database. So when our database is created, we want to create a table where we want to store the data about something. In this case, we want to store notes for doing operations with databases, with creating tables, inserting notes and similar
        //basically this string that we are going to define in here will be the query that will be executed
		String sqlQuery = "CREATE TABLE Note ( id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {  //Basically this onUpgrade() will be called when we change this database version
        String sqlQuery = "DROP TABLE IF EXISTS Note";
        sqLiteDatabase.execSQL(sqlQuery);
        onCreate(sqLiteDatabase);  //For performing the update
    }
}
