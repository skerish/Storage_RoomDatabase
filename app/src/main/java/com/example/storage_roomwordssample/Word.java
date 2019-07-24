package com.example.storage_roomwordssample;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 *  Variables can either be public or have a respective getter method.
 *  Tables name are case sensitive.
 */

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")    // Column name.
    private String mWord;

//    For auto-generated primary key.
//    @PrimaryKey(autoGenerate = true)
//    public int key;

    // NonNull annotation makes sure that mWord is never null.
    public Word(@NonNull String mWord) {
        this.mWord = mWord;
    }

    public String getWord() {
        return this.mWord;
    }
}
