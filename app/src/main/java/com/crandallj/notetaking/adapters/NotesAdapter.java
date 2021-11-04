package com.crandallj.notetaking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.crandallj.notetaking.databinding.NoteListItemBinding;
import com.crandallj.notetaking.models.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    ObservableArrayList<Note> notes;
    OnNoteSelectedListener listener;

    public static interface OnNoteSelectedListener {
        public void onSelected(Note note);

    }

    public NotesAdapter (ObservableArrayList<Note> notes, OnNoteSelectedListener listener){
        this.notes = notes;
        this.listener = listener;


        notes.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Note>>() {
            @Override
            public void onChanged(ObservableList<Note> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Note> sender, int positionStart, int itemCount) {
                NotesAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Note> sender, int positionStart, int itemCount) {
                NotesAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Note> sender, int fromPosition, int toPosition, int itemCount) {
                NotesAdapter.this.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Note> sender, int positionStart, int itemCount) {
                NotesAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NoteListItemBinding binding = NoteListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getBinding().titleListItem.setText(notes.get(position).getTitle().toString());
        Long time = notes.get(position).getTimestamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = new Date(time);
        String timeString = dateFormat.format(date);

        holder.getBinding().dateListItem.setText(timeString);
        holder.itemView.setOnClickListener(view -> {
            this.listener.onSelected(notes.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NoteListItemBinding binding;
        public ViewHolder(@NonNull NoteListItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public NoteListItemBinding getBinding() {
            return binding;
        }
    }
}
