package com.moaazfathyelneshawy.mynotes.View;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.moaazfathyelneshawy.mynotes.Constants;
import com.moaazfathyelneshawy.mynotes.R;

public class NoteModification extends AppCompatActivity {

    EditText etTitle, etDescription;
    NumberPicker npPriority;
    boolean isEdited = false;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_modification);
        etTitle = findViewById(R.id.title_et);
        etDescription = findViewById(R.id.desc_et);
        npPriority = findViewById(R.id.np_priority);

        npPriority.setMinValue(1);
        npPriority.setMaxValue(10);


        ActionBar actionBar = this.getSupportActionBar();
        try {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = this.getIntent();
        if (intent.hasExtra(Constants.ID)) {
            etTitle.setText(intent.getStringExtra(Constants.TITLE));
            etDescription.setText(intent.getStringExtra(Constants.Description));
            npPriority.setValue(intent.getIntExtra(Constants.PRIORITY, -1));
            id = intent.getIntExtra(Constants.ID, -1);
            isEdited = true;
            actionBar.setTitle(getString(R.string.edit_note));
        } else {
            isEdited = false;
            actionBar.setTitle(getString(R.string.add_note));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modification_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        int priority = npPriority.getValue();

        if (title.isEmpty()) {
            Toast.makeText(this, "Add Title first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (desc.isEmpty()) {
            Toast.makeText(this, "Add Description first", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent noteIntent = new Intent()
                .putExtra(Constants.TITLE, title)
                .putExtra(Constants.Description, desc)
                .putExtra(Constants.PRIORITY, priority);

        if (isEdited) {
            noteIntent.putExtra(Constants.ID, id);
        }

        setResult(RESULT_OK, noteIntent);
        finish();

    }
}
