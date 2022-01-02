package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {

    EditText edtTitle, edtDescription;

    Button btnCancel, btnSave;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

         
        final Intent intent = getIntent();

        linearLayout = findViewById(R.id.btn_holder);
        edtDescription = findViewById(R.id.edt_edit_descrption);
        edtTitle = findViewById(R.id.edt_edit_title);

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        //When the cancel button is clicked
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();  //if the user clicks on cancil, it would be the same if the user clicked on back button
            }
        });

        //When the save button is clicked---
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //We need to create a new note which is created with things that the user input in those new fields
                Note note = new Note(edtTitle.getText().toString(), edtDescription.getText().toString());  //Fetching the updated title and description from the textViews
                note.setId(intent.getIntExtra("id",1));  //Actually, we want to set that same ID that was on the note that we want to edit
				
                if (new NoteHandler(EditNote.this).update(note)){
                    Toast.makeText(EditNote.this, "Note updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditNote.this, "Failed updating", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        });

        //As we want to see our entire text(along  with the diting options) after clicking the pen icon we need to fetch the title and the description using intent and then we need to set that here
        edtDescription.setText(intent.getStringExtra("description"));
        edtTitle.setText(intent.getStringExtra("title"));

    }

    //We are overriding the method onBackPressed() so that we can add the animation after clicking on save or cancel ->the animation is basically- the save and cancel buttons are getting shown till the end that we don't want->We could have done that inside 'btnSave.setOnClickListener()' or 'btnCancel.setOnClickListener()', but hence in both the cases lastly we are calling onBackPressed() that's why we are setting it at the end
    @Override
    public void onBackPressed() {
        btnSave.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(linearLayout);
        super.onBackPressed();
    }
}