package com.example.storage_roomwordssample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

public class WordRepository {

    private Word_DAO mWordDao;
    private static AnonymousHelperClass anonymousHelperClass;


    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        anonymousHelperClass = new AnonymousHelperClass();   // To prevent null object reference.
    }

    /**
     * Add a wrapper method called getAllWords() that returns the cached words as LiveData.
     * Room executes all queries on a separate thread. Observed LiveData notifies the observer
     * when the data changes.
     */

    LiveData<List<Word>> getAllWords(){
        return mWordDao.getAllWords();
    }

    public void insert(Word word){
        new insertAsyncTask(mWordDao).execute(word);
    }

    public void deleteAll(){
        new deleteAsyncTask(mWordDao).execute();
    }

    public void deleteSingleWord(Word word){
        new deleteSingleAsyncTask(mWordDao).execute(word);
    }

    public void update(Word word){
        new updateAsyncTask(mWordDao).execute(word);
    }

    /**
     * Insert a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private Word_DAO mAsyncTaskDao;
        WeakReference<TextView> textViewWeakReference;

        insertAsyncTask(Word_DAO dao) {
            mAsyncTaskDao = dao;
            textViewWeakReference = new WeakReference<>(anonymousHelperClass.getTextView());
        }

        @Override
        protected Void doInBackground(Word... params) {
            if (mAsyncTaskDao.getAnyWord() == null){
                publishProgress();
            }
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            textViewWeakReference.get().setVisibility(View.GONE);
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void>{

        private Word_DAO mAsyncTaskDao2;
        WeakReference<TextView> textViewWeakReference;

        public deleteAsyncTask(Word_DAO mWordDao) {
            mAsyncTaskDao2 = mWordDao;
            textViewWeakReference = new WeakReference<>(anonymousHelperClass.getTextView());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao2.deleteAll();
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            textViewWeakReference.get().setVisibility(View.VISIBLE);
        }
    }

    private static class deleteSingleAsyncTask extends AsyncTask<Word, Void, Void>{

        private Word_DAO word_dao;
        WeakReference<TextView> textViewWeakReference;

        public deleteSingleAsyncTask(Word_DAO mWordDao) {
            word_dao = mWordDao;
            textViewWeakReference = new WeakReference<>(anonymousHelperClass.getTextView());
        }

        @Override
        protected Void doInBackground(Word... params) {
            word_dao.deleteSingleWord(params[0]);
            if (word_dao.getAnyWord() == null){
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            textViewWeakReference.get().setVisibility(View.VISIBLE);
        }
    }

    private static class updateAsyncTask extends AsyncTask<Word, Void, Void>{

        private Word_DAO word_dao;

        public updateAsyncTask(Word_DAO mWordDao) {
            word_dao = mWordDao;
        }

        @Override
        protected Void doInBackground(Word... params) {
            word_dao.update(params[0]);
            return null;
        }
    }
}
