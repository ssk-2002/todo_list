package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //sets the UI interface->inflate the main layout ('activity_main') for the activity

        imageButton = findViewById(R.id.img_add);  //getting the reference of the add icon

        imageButton.setOnClickListener(new View.OnClickListener() {  //when we click on Image Button (the add icon), we want to create or show the dialog_button for creating a new note
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  //needs to be type casted to LayoutInflater ->We can do the same using a different way also (whi9ch is in QnA->layoutInflater)
                View viewInput = inflater.inflate(R.layout.note_input, null, false);  //basically we are inflating (creating) a copy of our custom layout (note_input)-> will be used when we'll create the dialog

                // As we inflated the 'note_input' layout now we can use findViewById and can make changes to our custom layout and its subviews
                final EditText edtTitle = viewInput.findViewById(R.id.edt_title);
                final EditText edtDesription = viewInput.findViewById(R.id.edt_description);

                //Now we need to create finally that dialogue that will be shown after clicking on the add icon
                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)  //setting that view that we named 'note_input.xml'
                        .setTitle("Add Note")  //For the heading
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {  //positiveButton is the button that is used for finally adding the note after complete editing
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //We are collecting the note title and the description from the dialog button
                                String title = edtTitle.getText().toString();
                                String description = edtDesription.getText().toString();

                                //Creating a new note using the above fetched title and description
                                Note note = new Note(title, description);

                                //It's time for storing the note into our database, and for that we are using the create() method that we created for CRUD operations which is in NoteHandler
                                boolean isInserted = new NoteHandler(MainActivity.this).create(note); 

                                if (isInserted) {
                                    Toast.makeText(MainActivity.this, "Note saved..!", Toast.LENGTH_SHORT).show();
                                    loadNotes();  //If the note is successfully created then that should be shown in our list of notes, that's why we need to load the notes again-> that will basically update the list
                                } else {
                                    Toast.makeText(MainActivity.this, "Unable to save the note..!", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.cancel();  //in order to remove that dialogue from our screen after inserting the note
                            }
                        }).show();
            }
        });
        recyclerView = findViewById(R.id.recycler); 

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));  //RecyclerView needs a layout manager that we are setting as linear
        
		//Now let's see how to delete notes-> We want to delete notes by swiping it towards left or right-> for that we need to use something called 'ItemTouchHelper' which will detect when we are swiping holder inside our recyclerView
		ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {  //here we only need to put some values which will basically explain directions of our item in recycler view
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                new NoteHandler(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());  //deletes a note from Database
                notes.remove(viewHolder.getAdapterPosition());  // remove that note from our notes array list
				
				//now we need to notify our adapter that item is removed
                noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);  //we passed the Callback that we created
        itemTouchHelper.attachToRecyclerView(recyclerView);  //attached the itemTouchHelper to our recyclerView

        loadNotes();  //We will call loadNotes() when we open our app

    }

    //So now we need to set up our adapter so that we can set it to our recycler view

    public ArrayList<Note> readNotes() {  //So firstly, we need to basically load the notes to read our notes.
        ArrayList<Note> notes = new NoteHandler(this).readNotes();  //We are using NoteHandler's readNotes()
        return notes;
    }

    public void loadNotes() { //method for loading the notes or inflating them to a recycler view

        notes = readNotes();

        noteAdapter = new NoteAdapter(notes, this, new NoteAdapter.ItemClicked() {
            @Override
            public void onClick(int postion, View view) {  //If we'll click on any of the loaded notes then we should get the edit option
                editNote(notes.get(postion).getId(), view); 
            }
        });

        recyclerView.setAdapter(noteAdapter);
    }

    private void editNote(int noteId, View view) {
        NoteHandler noteHandler = new NoteHandler(this);

        Note note = noteHandler.readSingleNote(noteId);

        Intent intent = new Intent(this, EditNote.class);  //so that we can shift to the EditNote activity
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("id", note.getId());

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));  //for achieving that animation after clicking on itemView
        startActivityForResult(intent, 1, optionsCompat.toBundle());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loadNotes();
        }
    }
}