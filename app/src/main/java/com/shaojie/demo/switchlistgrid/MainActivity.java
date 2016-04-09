package com.shaojie.demo.switchlistgrid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GroupsRecyclerView mRecyclerView;
    private int mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (GroupsRecyclerView) findViewById(R.id.recycler_view_books);
        mRecyclerView.setHasFixedSize(true);
    }

}
