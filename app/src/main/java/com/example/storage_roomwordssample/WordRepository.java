package com.example.storage_roomwordssample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WordRepository {

    private Word_DAO mWordDao;
    private LiveData<List<Word>> mAllWords;
    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    /**
     * Add a wrapper method called getAllWords() that returns the cached words as LiveData.
     * Room executes all queries on a separate thread. Observed LiveData notifies the observer
     * when the data changes.
     */

    LiveData<List<Word>> getAllWords(){
        return mAllWords;
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

        insertAsyncTask(Word_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Word... params) {
            List<Word> words = mAsyncTaskDao.getAllWordsForChecking();
            boolean flag = false;
            for (int i = 0; i < words.size(); i++) {
                if (words.get(i).equals(params[0])){
                    flag = true;
                }
            }
            if (!flag){
                mAsyncTaskDao.insert(params[0]);
            }
            return null;
        }

    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void>{

        private Word_DAO mAsyncTaskDao2;

        public deleteAsyncTask(Word_DAO mWordDao) {
            mAsyncTaskDao2 = mWordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao2.deleteAll();
            return null;
        }
    }

    private static class deleteSingleAsyncTask extends AsyncTask<Word, Void, Void>{

        private Word_DAO word_dao;

        public deleteSingleAsyncTask(Word_DAO mWordDao) {
            word_dao = mWordDao;
        }

        @Override
        protected Void doInBackground(Word... params) {
            word_dao.deleteSingleWord(params[0]);
            return null;
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
