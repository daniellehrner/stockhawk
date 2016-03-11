package com.sam_chordas.android.stockhawk.rest;

import android.util.Log;

import com.db.chart.model.LineSet;

public class HistoricalDataClientImpl implements HistoricalDataClient {
    public LineSet getData(String symbol, int mDurationInDays) {
        String testString[] = {"Mo", "Di", "Mi"};
        float testFloat[] = {(float)50.0, (float)200.0, (float)100.0};

        Log.d("HistoricalDataClient", "test");

        return new LineSet(testString, testFloat);
    }
}
