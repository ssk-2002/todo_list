//A class which will handle those, let's say, transaction's or the saving, deleting and updating logic of the note objects.

package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class NoteHandler extends DatabaseHelper {

    public NoteHandler(Context context) {
        super(context);
    }

    //We will now create some methods which will be used for CRUD operations in database-> CRUD:  C create, R read, U update, D delete
	
    public boolean create(Note note) {  //For creating Notes

        ContentValues values = new ContentValues();
		
		//Now we will write or the insert this note object inside this database
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessfull = db.insert("Note", null, values) > 0; //Here we need to pass null for the null column hack, which means that our database is or our table is allowed to accept empty rows so we can omit title or description or both of those->Here row is basically one note object that have ID, title and description.

        db.close();  //Closing the Database
        return isSuccessfull;  //Returns if the insertion was successful or not
    }

    //Now is the time to create a method which will return a list of notes 
    public ArrayList<Note> readNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Note ORDER BY id ASC";  //create new SQL query in here, which will fetch those notes

        SQLiteDatabase db = this.getWritableDatabase();
		
        Cursor cursor = db.rawQuery(sqlQuery, null);   //Curser is for the navigating through the database.
		
		//Now we need to move this cursor as the first row in our database. So we would firstly check basically is it possible to be moved to the first one. For example, our database or if our table is empty, it can be moved anywhere.
        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                //Now is the time to add note that we have received to the array list
                Note note = new Note(title, description);
                note.setId(id);  //Set the note id manually
                notes.add(note);  //now add this note to the notes list
            } while (cursor.moveToNext());

            //Last thing is closing curser so that we avoid memory leaks and closing database
            cursor.close();
            db.close();
        }
        return notes;
    }

    //Now, we need to also create a method for fetching a single note. We are using that one when we want to edit specific note.
    public Note readSingleNote(int id) {  //->we will find that note by using ID of the note.
        Note note = null;
        String sqlQuery = "SELECT * FROM Note WHERE id=" + id;  //We write in this way as we want to concatenate the id with the SQL statement
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {  //Again we are doing the same thing for fetching data->but here we are dealing with a single row only
            int noteId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            note = new Note(title, description);
            note.setId(noteId);
        }
        cursor.close();
        db.close();
        return note;
    }

    //Now, let's see how to update current note
    public boolean update(Note note) {  //note: The note that is to be updated

        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccessfull = db.update("Note", values, "id='" + note.getId() + "'", null) > 0;
        db.close();
        return isSuccessfull;
    }

    //last thing that we want to do is to delete note when we want that
    public boolean delete(int id) {
        boolean isDeleted;
        SQLiteDatabase db = this.getWritableDatabase();
        isDeleted = db.delete("Note", "id='" + id + "'", null) > 0;
        db.close();
        return isDeleted;
    }

}
