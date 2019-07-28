package com.example.storage_roomwordssample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Listing the entities class or classes creates corresponding tables in the database.
 * exportSchema keeps a history of schema versions.
 */

@Database(entities = {Word.class}, version = 4, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {

    // Required abstract method of DAO type which'll be defined by System itself on build.
    public abstract Word_DAO wordDao();

    private static WordRoomDatabase INSTANCE;

    // Create the WordRoomDatabase as a singleton to prevent having multiple instances of the
    // database opened at the same time.
    public static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (WordRoomDatabase.class){
                if (INSTANCE == null){
                    // Create Database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                           // .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Use to implement any action or view on opening the database.
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     *  Note: This functionality will only work whenever onCreate() is called.
     *        For global implementation, need to implement it in every database query AsyncTask.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Boolean, Void> {

        private final Word_DAO mDao;
   //     String[] words = {"Jerry", "Jamie", "Jason"};
        AnonymousHelperClass anonymousHelperClass = new AnonymousHelperClass();
        WeakReference<TextView> textViewWeakReference;

        PopulateDbAsync(WordRoomDatabase db) {
            mDao = db.wordDao();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewWeakReference = new WeakReference<>(anonymousHelperClass.getTextView());
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (mDao.getAnyWord() == null){
                publishProgress(true);
//                for (int i = 0; i < words.length; i++) {
//                    Word word = new Word(words[i]);
//                    mDao.insert(word);
//                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            if (values[0]){
                textViewWeakReference.get().setVisibility(View.VISIBLE);
            }
            else{
                textViewWeakReference.get().setVisibility(View.GONE);
            }
        }
    }
}
