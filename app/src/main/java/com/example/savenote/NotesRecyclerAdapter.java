package com.example.savenote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private ArrayList<Notes> notesList;
    private Context context;

    public NotesRecyclerAdapter(ArrayList<Notes> notesList, Context context) {
        Log.d("MainActivity", "Notes list in adapter is " + notesList.size());
        this.notesList = notesList;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public String docId;
        public int isActive;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isActive == 1) {
                        // Open Active note
                        Intent noteDetailIntent = new Intent(context, NewnoteActivity.class);
                        noteDetailIntent.putExtra("docId", docId);
                        noteDetailIntent.putExtra("title", title.getText());
                        noteDetailIntent.putExtra("content", content.getText());
                        context.startActivity(noteDetailIntent);
                    } else {
                        // Show the doc to user in dialog , Give option to restore
                        AlertDialog alertDialog = new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).
                                setTitle(title.getText())
                                .setMessage(content.getText())
                                .setPositiveButton("Restore Note", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Update note status in db and remove it from trash list
                                        DBHelper dbHelper = new DBHelper(context);
                                        dbHelper.updateNote(docId, title.getText().toString(), content.getText().toString(), 1);

                                        // Update the list
                                        ((MainActivity) context).notesList.remove(getAdapterPosition());
                                        ((MainActivity) context).adapter.notifyDataSetChanged();

                                        if (notesList.size() == 0) {
                                            // NO items , show zero layout
                                            ((MainActivity) context).onResume();

                                        }

                                    }
                                })
                                .setNegativeButton("Delete forever", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Delete the note from db , update the list
                                        DBHelper dbHelper = new DBHelper(context);
                                        dbHelper.deleteNote(docId);

                                        // Update the list
                                        ((MainActivity) context).notesList.remove(getAdapterPosition());
                                        ((MainActivity) context).adapter.notifyDataSetChanged();

                                        if (notesList.size() == 0) {
                                            // NO items , show zero layout
                                            ((MainActivity) context).onResume();

                                        }
                                    }
                                }).create();
                        alertDialog.show();

                    }

                }
            });
        }


    }

    public void add(int position, Notes note) {
        notesList.add(position, note);
        notifyItemInserted(position);
    }

    public void remove(Notes note) {
        int position = notesList.indexOf(note);
        notesList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public NotesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d("MainActivity", "Setting bholder for " + position);
        holder.title.setText(notesList.get(position).getTitle());
        holder.content.setText(notesList.get(position).getContent());
        holder.docId = notesList.get(position).getDocId();
        holder.isActive = notesList.get(position).getIsActive();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
