package com.example.storage_roomwordssample;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

/**
 *  Dao can either be an abstract class or an Interface.
 */

@Dao
public interface Word_DAO {

    @Insert
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    //LiveData return type is used because Room generates all necessary code to update the
    // LiveData when the database is updated.

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    LiveData<List<Word>> getAllWords();

}
