package com.example.storage_roomwordssample;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import java.util.List;

/**
 *  Never pass context into ViewModel instances.
 *  Do not store Activity, Fragment, or View instances or their Context in the ViewModel.
 */

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<Word>> getAllWords(){
        return mAllWords;
    }

    public void insert(Word word){
        mRepository.insert(word);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void deleteSingleWord(Word word){
        mRepository.deleteSingleWord(word);
    }

    public void update(Word word){
        mRepository.update(word);
    }

}
