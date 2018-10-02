package com.curiousca.squiz.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.curiousca.squiz.Activities.MainActivity;
import com.curiousca.squiz.QuizClickInterface;
import com.curiousca.squiz.R;
import com.curiousca.squiz.RecyclerViewAdapter;

import java.util.ArrayList;


public class RecyclerViewActivity extends AppCompatActivity implements QuizClickInterface {

    private static final String TAG = "RecyclerViewActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, RecyclerViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_vertical_view);
        Log.d(TAG, "onCreate: started.");

        initImageBitmaps();
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mNames.add("History");
        mNames.add("Programming");
        mNames.add("Computer graphics");
        mNames.add("OS");
        mNames.add("DBMS");
        mNames.add("DS");
        mNames.add("Economics");
        mNames.add("DSP");
        mNames.add("Data Structure");
        mNames.add("Physics");
        mNames.add("Applied Mechanics");
        mNames.add("C");
        mNames.add("C++");
        mNames.add("Java");
        mNames.add("Android");

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        //vertical recycler view
        RecyclerView recyclerView = findViewById(R.id.recycler_vertical_view);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onRecyclerItemClick(int adapterPosition, String categoryName) {
        MainActivity.start(this, categoryName, adapterPosition + 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}






















