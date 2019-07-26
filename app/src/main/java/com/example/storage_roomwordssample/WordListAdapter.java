package com.example.storage_roomwordssample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Word> mWords;
    private static MyClickListener myClickListener;


    WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        Word current = mWords.get(position);
        holder.wordItemView.setText(current.getWord());
    }

    // Will be called first.
    void setWords(List<Word> words){
        mWords = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }

    // For ItemTouchHelper.
    public Word getWordAtPosition(int position){
        return mWords.get(position);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {

        private final TextView wordItemView;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // for passing values to MyClickListener method.
                    myClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public void setCustomItemClickListener(MyClickListener myClickListener){
        WordListAdapter.myClickListener = myClickListener;
    }

}
