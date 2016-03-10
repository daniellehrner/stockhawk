package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sam_chordas.android.stockhawk.R;

public class LineGraphActivity extends AppCompatActivity {
    public static String KEY_SYM = "LineGraphActivity.KEY_SYM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra(KEY_SYM);

    }

}
