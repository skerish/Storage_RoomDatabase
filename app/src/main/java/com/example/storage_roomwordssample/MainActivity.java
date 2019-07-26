package com.example.storage_roomwordssample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private WordViewModel mWordViewModel;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    TextView textView;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_WORD_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_WORD = "extra_word_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = findViewById(R.id.empty_database_text);

        recyclerView = findViewById(R.id.recyclerView);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Use ViewModelProviders to associate your ViewModel with your UI controller.
        // When your app first starts, the ViewModelProviders class creates the ViewModel.
        // When the activity is destroyed, for example through a configuration change,
        // the ViewModel persists. When the activity is re-created, the ViewModelProviders
        // return the existing ViewModel.
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        // When the observed data changes while the activity is in the foreground,
        // the onChanged() method is invoked and updates the data cached in the adapter.
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word myWord = adapter.getWordAtPosition(position);
                Toast.makeText(MainActivity.this, myWord.getWord() + " Deleted", Toast.LENGTH_SHORT).show();
                mWordViewModel.deleteSingleWord(myWord);
            }
        });

        helper.attachToRecyclerView(recyclerView);



        adapter.setCustomItemClickListener(new MyClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Word word = adapter.getWordAtPosition(position);
                launchUpdateWordMethod(word);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        }
        else if(requestCode == UPDATE_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            String word_data = data.getStringExtra(NewWordActivity.EXTRA_REPLY);
            int id = data.getIntExtra(NewWordActivity.EXTRA_REPLY_ID, -1);
            if (id != -1){
                mWordViewModel.update(new Word(word_data, id));
            }
            else {
                Toast.makeText(this, "Error! Unable to Update", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            mWordViewModel.deleteAll();
            Snackbar.make(fab, "Database Cleared", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchUpdateWordMethod(Word word) {
        Intent intent = new Intent(this, NewWordActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_WORD, word.getWord());
        intent.putExtra(EXTRA_DATA_ID, word.getKey());
        startActivityForResult(intent, UPDATE_WORD_ACTIVITY_REQUEST_CODE);
    }

}
