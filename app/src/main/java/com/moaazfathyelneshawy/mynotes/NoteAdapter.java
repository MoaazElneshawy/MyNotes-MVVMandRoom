package com.moaazfathyelneshawy.mynotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moaazfathyelneshawy.mynotes.RoomPackage.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_note, viewGroup, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        Note note = notes.get(i);
        noteViewHolder.title.setText(note.getTitle());
        noteViewHolder.description.setText(note.getDescription());
        noteViewHolder.priority.setText(String.valueOf(note.getPriority()));
    }

    public Note getNote(int position) {
        return notes.get(position);
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public int getItemCount() {
        if (notes != null)
            return notes.size();
        else return 0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, priority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title_tv);
            description = itemView.findViewById(R.id.item_description_tv);
            priority = itemView.findViewById(R.id.item_priority_tv);
        }
    }
}
