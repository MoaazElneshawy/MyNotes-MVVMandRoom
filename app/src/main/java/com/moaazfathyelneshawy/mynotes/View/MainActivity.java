package com.moaazfathyelneshawy.mynotes.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.moaazfathyelneshawy.mynotes.Constants;
import com.moaazfathyelneshawy.mynotes.NoteAdapter;
import com.moaazfathyelneshawy.mynotes.R;
import com.moaazfathyelneshawy.mynotes.RoomPackage.Note;
import com.moaazfathyelneshawy.mynotes.ViewModel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvNotes;
    FloatingActionButton fab;
    NoteViewModel noteViewModel;
    TextView tvHint;
    List<Note> noteList;
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList = new ArrayList<>();
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        tvHint = findViewById(R.id.add_hint_tv);
        rvNotes = findViewById(R.id.notes_rv);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.modify_fab);
        adapter = new NoteAdapter();
        rvNotes.setAdapter(adapter);

        noteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                if (notes != null && notes.size() > 0) {
                    noteList = notes;
                    adapter.setNotes(notes);
                    rvNotes.setVisibility(View.VISIBLE);
                    tvHint.setVisibility(View.GONE);
                } else {
                    rvNotes.setVisibility(View.GONE);
                    tvHint.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteModification.class);
                startActivityForResult(intent, Constants.ADD_REQUEST_CODE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Note note = adapter.getNote(viewHolder.getAdapterPosition());
                noteViewModel.delete(note);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvNotes);

        RecycleClick.addTo(rvNotes)
                .setOnItemClickListener(new RecycleClick.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        
                        Intent intent = new Intent(MainActivity.this, NoteModification.class)
                                .putExtra(Constants.ID, noteList.get(position).getId())
                                .putExtra(Constants.TITLE, noteList.get(position).getTitle())
                                .putExtra(Constants.Description, noteList.get(position).getDescription())
                                .putExtra(Constants.PRIORITY, noteList.get(position).getPriority());

                        startActivityForResult(intent, Constants.EDIT_REQUEST_CODE);
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_delete_all:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(Constants.TITLE);
            String desc = data.getStringExtra(Constants.Description);
            int priority = data.getIntExtra(Constants.PRIORITY, 1);
            Note note = new Note(title, desc, priority);
            noteViewModel.insert(note);
        } else if (requestCode == Constants.EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(Constants.TITLE);
            String desc = data.getStringExtra(Constants.Description);
            int priority = data.getIntExtra(Constants.PRIORITY, 1);
            int id = data.getIntExtra(Constants.ID, -1);
            if (id == -1) {
                Toast.makeText(this, "No Notes Updated", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Note note = new Note(title, desc, priority);
                note.setId(id);
                noteViewModel.update(note);
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No Notes Added", Toast.LENGTH_SHORT).show();
        }
    }
}
