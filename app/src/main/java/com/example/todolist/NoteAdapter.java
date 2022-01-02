package com.example.todolist;

import android.content.Context;
import android.graphics.Color;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    ArrayList<Note> notes;
    Context context; //will be used for inflating view
    ItemClicked itemClicked;
    ViewGroup parent;
    private int lastSelectedPosition = -1;

    public NoteAdapter(ArrayList<Note> arrayList, Context context, ItemClicked itemClicked){ //initialize those values so that we can communicate from the adapter with the main activity
        notes = arrayList;
        this.context = context;
        this.itemClicked = itemClicked;  

    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {   //This method is called during the creation of the note Holders
        View view = LayoutInflater.from(context).inflate(R.layout.note_holder,parent,false);
        this.parent = parent;

        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {  //Here we want to set the data from the notes at specific position to our text and similar.
        if (position == lastSelectedPosition){
            holder.title.setTextColor(Color.parseColor("#000000"));
        }else {
            holder.title.setTextColor(Color.parseColor("#FFFFFF"));
        }
        holder.title.setText(notes.get(position).getTitle());
        holder.description.setText(notes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView description;
        ImageView imgEdit;
        public NoteHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_note_name);
            description = itemView.findViewById(R.id.txt_note_description);
            imgEdit = itemView.findViewById(R.id.img_edit);
			
			//we are setting the onClcikListener() for the imageView and the itemView seperately as we want to perform differnent tasks
			
            itemView.setOnClickListener(new View.OnClickListener() { //if the itemView (anything but this pen image) is clicked we want to either expand the holder or collapse . HOW? ->Basically, we need to change attribute, which is called max_lines
                @Override
                public void onClick(View view) {
                    title.setTextColor(Color.parseColor("#000000"));
                    lastSelectedPosition = getAdapterPosition();

                    if (description.getMaxLines() == 1){  //if the maxLine is set to 1 that means we need to enlarge it after being clicked, else we need to collapse it
                        description.setMaxLines(Integer.MAX_VALUE);
                    }else {
                        description.setMaxLines(1);
                    }
                    notifyDataSetChanged();
                    TransitionManager.beginDelayedTransition(parent);
                }
            });
            imgEdit.setOnClickListener(new View.OnClickListener() {  //if the imageView (the pen Button) is clocked there will be an animation
                @Override
                public void onClick(View view) {
                    itemClicked.onClick(getAdapterPosition(),itemView); //We are passing the entire itemView (not the imageView) because we want to perform animation
                }
            });

        }
    }

    interface ItemClicked{
        void onClick(int position, View view);
    }
}
