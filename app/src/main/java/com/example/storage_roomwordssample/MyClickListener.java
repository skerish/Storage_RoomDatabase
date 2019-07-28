package com.example.storage_roomwordssample;

import android.view.View;

public interface MyClickListener {

    /**
     *  Data will be passed from ViewHolder's onClick method.
     *  Which can then be accessible in MainActivity class.
     *
     *  Note: MyClickListener must be initialize first.
     */

    void onItemClick(View v, int position);

}
