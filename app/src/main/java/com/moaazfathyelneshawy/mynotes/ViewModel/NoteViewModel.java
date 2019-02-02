package com.moaazfathyelneshawy.mynotes.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.moaazfathyelneshawy.mynotes.RoomPackage.Note;
import com.moaazfathyelneshawy.mynotes.RoomPackage.NoteDao;
import com.moaazfathyelneshawy.mynotes.RoomPackage.NoteDatabase;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteDao noteDao;
    private LiveData<List<Note>> notes;

    public NoteViewModel(Application application) {
        super(application);
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        notes = noteDao.getNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void insert(Note note) {
        new InsertAsyncTask(noteDao).execute(note);
    }

    private class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        public InsertAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    public void update(Note note) {
        new UpdateAsyncTask(noteDao).execute(note);
    }

    private class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public UpdateAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    public void delete(Note note) {
        new DeleteAsyncTask(noteDao).execute(note);
    }

    private class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public DeleteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    public void deleteAllNotes() {
        new DeleteAllAsyncTask(noteDao).execute();
    }

    private class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        public DeleteAllAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
