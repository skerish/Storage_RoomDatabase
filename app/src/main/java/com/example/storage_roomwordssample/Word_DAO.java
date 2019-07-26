package com.example.storage_roomwordssample;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 *  Dao can either be an abstract class or an Interface.
 */

@Dao
public interface Word_DAO {

    @Insert
    void insert(Word word);

    @Delete
    void deleteSingleWord(Word word);

    @Update
    void update(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    //LiveData return type is used because Room generates all necessary code to update the
    // LiveData when the database is updated.

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM word_table")
    List<Word> getAllWordsForChecking();

    @Query("SELECT * FROM word_table LIMIT 1")
    Word getAnyWord();
}
